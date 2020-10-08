/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import static io.proximax.dfms.utils.HttpUtils.encode;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.proximax.dfms.ContractClientServices;
import io.proximax.dfms.ServiceBase;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.http.dtos.CidListDTO;
import io.proximax.dfms.http.dtos.ContractWapperDTO;
import io.proximax.dfms.http.dtos.VerifyResultDTO;
import io.proximax.dfms.model.contract.Amendment;
import io.proximax.dfms.model.contract.Contract;
import io.proximax.dfms.model.contract.ContractDuration;
import io.proximax.dfms.model.contract.ContractOptions;
import io.proximax.dfms.model.contract.VerificationError;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
/**
 * Contract client services implementation using HTTP protocol
 */
public class ContractHttp extends HttpRepository<ServiceBase> implements ContractClientServices {

   private static final String URL_COMPOSE = "contract/compose";
   private static final String URL_LS = "contract/ls";
   private static final String URL_GET = "contract/get";
   private static final String URL_AMENDS = "contract/amends";
   private static final String URL_VERIFY = "/contract/verify";
   private static final String URL_FINISH = "/contract/finish";
   
   /**
    * create new instance
    * 
    * @param api the storage API
    * @param apiPath the path to the API on the node
    * @param client the HTTP client to be used to execute requests
    */
   public ContractHttp(ServiceBase api, String apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      super(api, Optional.of(apiPath), client, longPollingClient);
   }

   @Override
   public Observable<Contract> compose(BigInteger space, ContractDuration duration, ContractOptions options) {
      HttpUrl url = buildUrl(URL_COMPOSE, options.asOptionMap(), space.toString(), duration.encode()).build();
      // make the request
      return makePostObservable(url, true)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, ContractWapperDTO.class))
            .map(ContractWapperDTO::getContract)
            .map(Contract::fromDto);
   }

   @Override
   public Observable<Contract> get(Cid id) {
      HttpUrl url = buildUrl(URL_GET, encode(id)).build();
      // make the request
      return makePostObservable(url, false).map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, ContractWapperDTO.class)).map(ContractWapperDTO::getContract)
            .map(Contract::fromDto);
   }

   @Override
   public Observable<List<Cid>> list() {
      HttpUrl url = buildUrl(URL_LS).build();
      // make the request
      return makePostObservable(url, false).map(this::mapStringOrError).map(str -> getGson().fromJson(str, CidListDTO.class))
            .map(CidListDTO::getIds).flatMapIterable(list -> list).map(Cid::decode).toList().toObservable();
   }

   @Override
   public Observable<Amendment> amendments(Cid id) {
      HttpUrl url = buildUrl(URL_AMENDS, encode(id)).build();
      // make the request
      return makePostObservable(url, true)
            .map(this::mapRespBodyOrError)
            .observeOn(Schedulers.io())
            .flatMap(HttpRepository::longPollingObserver)
            .map(str -> getGson().fromJson(str, Amendment.class));
   }

   @Override
   public Observable<List<VerificationError>> verify(Cid id) {
      HttpUrl url = buildUrl(URL_VERIFY, encode(id)).build();
      // make the request
      return makePostObservable(url, true)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, VerifyResultDTO.class))
            .map(VerificationError::fromDto)
            .toList().toObservable();
   }

   @Override
   public Completable finish(Cid id) {
      HttpUrl url = buildUrl(URL_FINISH, encode(id)).build();
      return makePostCompletable(url);
   }

}
