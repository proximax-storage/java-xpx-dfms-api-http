/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import static io.proximax.dfms.utils.HttpUtils.encode;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import io.proximax.dfms.ContractRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.http.dtos.CidListDTO;
import io.proximax.dfms.http.dtos.ContractWapperDTO;
import io.proximax.dfms.model.contract.Contract;
import io.proximax.dfms.model.contract.UpdatesSubscription;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
/**
 * Contract repository implementation using HTTP protocol
 */
public class ContractHttp extends HttpRepository<StorageApi> implements ContractRepository {

   private static final String URL_COMPOSE = "contract/compose";
   private static final String URL_LS = "contract/ls";
   private static final String URL_GET = "contract/get";
   private static final String URL_AMENDS = "contract/amends";

   /**
    * create new instance
    * 
    * @param api the storage API
    * @param apiPath the path to the API on the node
    * @param client the HTTP client to be used to execute requests
    */
   public ContractHttp(StorageApi api, String apiPath, OkHttpClient client) {
      super(api, Optional.of(apiPath), client);
   }

   @Override
   public Observable<Contract> compose(BigInteger space, Duration duration) {
      // TODO what is the duration field? openapi says: Drive contract duration. Might be in millisecconds or blocks.
      HttpUrl url = buildUrl(URL_COMPOSE, space.toString(), Long.toString(duration.toMillis())).build();
      // make the request
      return makePostObservable(url).map(this::mapStringOrError).map(str -> getGson().fromJson(str, Contract.class));
   }

   @Override
   public Observable<Contract> get(Cid id) {
      HttpUrl url = buildUrl(URL_GET, encode(id)).build();
      // make the request
      return makePostObservable(url).map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, ContractWapperDTO.class)).map(ContractWapperDTO::getContract)
            .map(Contract::fromDto);
   }

   @Override
   public Observable<List<Cid>> list() {
      HttpUrl url = buildUrl(URL_LS).build();
      // make the request
      return makePostObservable(url).map(this::mapStringOrError).map(str -> getGson().fromJson(str, CidListDTO.class))
            .map(CidListDTO::getIds).flatMapIterable(list -> list).map(Cid::decode).toList().toObservable();
   }

   @Override
   public Observable<UpdatesSubscription> amendments(Cid id) {
      HttpUrl url = buildUrl(URL_AMENDS, encode(id)).build();
      // make the request
      return makePostObservable(url).map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, UpdatesSubscription.class));
   }

   @Override
   public Completable startAccepting() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Completable stopAccepting() {
      // TODO Auto-generated method stub
      return null;
   }

}
