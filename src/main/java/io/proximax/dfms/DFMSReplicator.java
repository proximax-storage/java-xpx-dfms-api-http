/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.net.URL;

import io.proximax.dfms.http.repos.ContractHttp;
import io.proximax.dfms.http.repos.DriveHttp;
import io.proximax.dfms.http.repos.NetworkHttp;
import io.proximax.dfms.http.repos.ReplicatorHttp;
import okhttp3.OkHttpClient;

/**
 * Replicator node provides access to {@link ReplicatorRepository}, {@link ContractRepository} and {@link NetworkRepository}
 */
public class DFMSReplicator extends ServiceBase {

   /**
    * @param nodeUrl
    * @param apiPath
    * @param client
    * @param longPollingClient
    */
   public DFMSReplicator(URL nodeUrl, String apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      super(nodeUrl, apiPath, client, longPollingClient);
   }

   /**
    * @param nodeUrl
    * @param apiPath
    */
   public DFMSReplicator(URL nodeUrl, String apiPath) {
      super(nodeUrl, apiPath);
   }

   /**
    * @param nodeUrl
    */
   public DFMSReplicator(URL nodeUrl) {
      super(nodeUrl);
   }

   public ReplicatorRepository createReplicatorRepository() {
      return new ReplicatorHttp(this, getApiPath(), getClient(), getLongPollingClient());
   }

   public DriveRepository createDriveRepository() {
      return new DriveHttp(this, getApiPath(), getClient(), getLongPollingClient());
   }

   public ContractRepository createContractRepository() {
      return new ContractHttp(this, getApiPath(), getClient(), getLongPollingClient());
   }

   public NetworkRepository createNetworkRepository() {
      return new NetworkHttp(this, getApiPath(), getClient(), getLongPollingClient());
   }

}
