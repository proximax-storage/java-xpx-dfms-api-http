/*
 * Copyright 2020 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.net.URL;

import io.proximax.dfms.http.repos.ReplicatorHttp;
import okhttp3.OkHttpClient;

/**
 * Central access point to the services provided by an DFMS Replicator node
 */
public class DFMSReplicator extends DFMSBaseNode {

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

   /**
    * create service instance allowing access to the node's contract replicator end-points
    * 
    * @return new instance
    */
   public ContractReplicatorServices createContractReplicatorServices() {
      return new ReplicatorHttp(this, getApiPath(), getClient(), getLongPollingClient());
   }
}
