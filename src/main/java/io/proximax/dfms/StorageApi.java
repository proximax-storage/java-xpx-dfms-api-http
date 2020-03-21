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
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

/**
 * Central access point to the storage API. Provides factories for DFMS service repositories
 */
public class StorageApi implements ServiceNode {

   public static final String API_PATH = "api/v1";

   /** URL of the node */
   private final URL nodeUrl;
   /** the path to the API on the node */
   private final String apiPath;
   private final OkHttpClient client;

   /**
    * Create new instance running at specified URL
    * 
    * @param nodeUrl the URL of the node
    * @param apiPath the path to the API
    */
   public StorageApi(URL nodeUrl, String apiPath) {
      this.nodeUrl = nodeUrl;
      this.apiPath = apiPath;
      this.client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(Level.BASIC))
            .build();
   }

   /**
    * Create new instance running at specified URL with default API path
    * 
    * @param nodeUrl the URL of the node
    */
   public StorageApi(URL nodeUrl) {
      this(nodeUrl, API_PATH);
   }

   /**
    * create drive repository
    * 
    * @return new instance
    */
   public DriveRepository createDriveRepository() {
      return new DriveHttp(this, apiPath, client);
   }

   /**
    * create contract repository
    * 
    * @return new instance
    */
   public ContractRepository createContractRepository() {
      return new ContractHttp(this, apiPath, client);
   }

   public NetworkRepository createNetworkRepository() {
      return new NetworkHttp(this, apiPath, client);
   }

   @Override
   public URL getUrl() {
      return nodeUrl;
   }

   /**
    * @return the API path
    */
   public String getApiPath() {
      return apiPath;
   }
}
