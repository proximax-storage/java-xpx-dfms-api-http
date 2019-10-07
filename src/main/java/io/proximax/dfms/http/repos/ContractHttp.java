/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Optional;

import io.ipfs.cid.Cid;
import io.proximax.dfms.ContractRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.contract.ContractResponse;
import io.proximax.dfms.contract.InviteSubscription;
import io.proximax.dfms.contract.UpdatesSubscription;
import io.proximax.dfms.http.HttpRepository;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;

/**
 * Contract repository implementation using HTTP protocol
 */
public class ContractHttp extends HttpRepository<StorageApi> implements ContractRepository {

   /**
    * create new instance
    * 
    * @param api the storage API
    * @param apiPath the path to the API on the node
    */
   public ContractHttp(StorageApi api, String apiPath) {
      super(api, Optional.of(apiPath), new OkHttpClient());
   }

   @Override
   public Observable<ContractResponse> get(Cid id) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Completable join(Cid id) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<Cid> list(Cid id) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<UpdatesSubscription> updates(Cid id) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<InviteSubscription> invites() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<ContractResponse> compose(BigInteger space, Duration duration) {
      // TODO Auto-generated method stub
      return null;
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
