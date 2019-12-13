/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Optional;

import io.proximax.Cid;
import io.proximax.dfms.ContractRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.contract.Contract;
import io.proximax.dfms.contract.UpdatesSubscription;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.http.responses.CidListResponse;
import io.proximax.dfms.http.responses.ContractResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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
    */
   public ContractHttp(StorageApi api, String apiPath, OkHttpClient client) {
      super(api, Optional.of(apiPath), client);
   }

   @Override
   public Observable<Contract> compose(BigInteger space, Duration duration) {
      // TODO what is the duration field? openapi says: Drive contract duration. Might be in millisecconds or blocks.
      HttpUrl url = buildUrl(URL_COMPOSE, space.toString(), Long.toString(duration.toMillis())).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      // make the request
      return makeRequest(request).map(HttpRepository::mapStringOrError)
            .map(str -> getGson().fromJson(str, Contract.class));
   }

   @Override
   public Observable<Contract> get(Cid id) {
      HttpUrl url = buildUrl(URL_GET, id.toString()).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      // make the request
      return makeRequest(request).map(HttpRepository::mapStringOrError)
            .map(str -> getGson().fromJson(str, ContractResponse.class))
            .map(ContractResponse::getContract)
            .map(Contract::fromDto);
   }

   @Override
   public Observable<Cid> list() {
      HttpUrl url = buildUrl(URL_LS).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      // make the request
      return makeRequest(request).map(HttpRepository::mapStringOrError)
            .map(str -> getGson().fromJson(str, CidListResponse.class)).map(CidListResponse::getIds).flatMapIterable(list -> list)
            .map(Cid::decode);
   }

   @Override
   public Observable<UpdatesSubscription> amendments(Cid id) {
      HttpUrl url = buildUrl(URL_AMENDS, id.toString()).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      // make the request
      return makeRequest(request).map(HttpRepository::mapStringOrError)
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
