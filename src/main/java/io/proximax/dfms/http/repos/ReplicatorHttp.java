/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import static io.proximax.dfms.utils.HttpUtils.encode;

import java.util.Optional;

import io.proximax.dfms.ReplicatorRepository;
import io.proximax.dfms.ServiceBase;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.http.dtos.InviteDTO;
import io.proximax.dfms.http.dtos.InviteWrapperDTO;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
/**
 * Contract repository implementation using HTTP protocol
 */
public class ReplicatorHttp extends HttpRepository<ServiceBase> implements ReplicatorRepository {

   private static final String URL_INVITES = "contract/invites";
   private static final String URL_ACCEPT = "contract/accept";
   

   /**
    * create new instance
    * 
    * @param api the storage API
    * @param apiPath the path to the API on the node
    * @param client the HTTP client to be used to execute requests
    */
   public ReplicatorHttp(ServiceBase api, String apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      super(api, Optional.of(apiPath), client, longPollingClient);
   }

   @Override
   public Observable<InviteDTO> invites() {
      HttpUrl url = buildUrl(URL_INVITES).build();
      // make the request
      return makeGetObservable(url, true)
            .map(this::mapRespBodyOrError)
            .observeOn(Schedulers.io())
            .flatMap(ReplicatorHttp::longPollingObserver)
            // map the line to invite wrapper dto
            .map(str -> getGson().fromJson(str, InviteWrapperDTO.class))
            // map the wrapper to wrapped invite
            .map(InviteWrapperDTO::getInvite);
   }
   
   @Override
   public Completable accept(Cid id) {
      HttpUrl url = buildUrl(URL_ACCEPT, encode(id)).build();
      // make the request
      return makePostCompletable(url);

   }
}
