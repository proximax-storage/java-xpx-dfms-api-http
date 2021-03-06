/*
 * Copyright 2020 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.net.URL;

import io.proximax.dfms.http.repos.DriveHttp;
import io.proximax.dfms.http.repos.SuperContractHttp;
import okhttp3.OkHttpClient;

/**
 * Central access point to the services provided by an DFMS Client node
 */
public class DFMSClient extends DFMSBaseNode {

   /**
    * @param nodeUrl
    * @param apiPath
    * @param client
    * @param longPollingClient
    */
   public DFMSClient(URL nodeUrl, String apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      super(nodeUrl, apiPath, client, longPollingClient);
   }

   /**
    * @param nodeUrl
    * @param apiPath
    */
   public DFMSClient(URL nodeUrl, String apiPath) {
      super(nodeUrl, apiPath);
   }

   /**
    * @param nodeUrl
    */
   public DFMSClient(URL nodeUrl) {
      super(nodeUrl);
   }
   
   /**
    * create service instance allowing access to the node's super contract end-points
    * 
    * @return new instance
    */
   public SuperContractServices createSuperContractServices() {
      return new SuperContractHttp(this, getApiPath(), getClient(), getLongPollingClient());
   }
   
   /**
    * create service instance allowing access to the node's drive end-points
    * 
    * @return new instance
    */
   public DriveServices createDriveServices() {
      return new DriveHttp(this, getApiPath(), getClient(), getLongPollingClient());
   }
}
