/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

/**
 * Base of DFMS services
 */
public abstract class ServiceBase implements ServiceNode {

   /** default API path */
   public static final String API_PATH = "api/v1";

   /** URL of the node */
   private final URL nodeUrl;
   /** the path to the API on the node */
   private final String apiPath;
   /** client initialized for standard socket communication */
   private final OkHttpClient client;
   /** client initialized for infinite read timeouts */
   private final OkHttpClient longPollingClient;

   /**
    * Create new storage API with manually defining all the parameters
    * 
    * @param nodeUrl URL of the node
    * @param apiPath API path prefixed to all requests - {@link #API_PATH}
    * @param client
    * @param longPollingClient
    */
   public ServiceBase(URL nodeUrl, String apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      this.nodeUrl = nodeUrl;
      this.apiPath = apiPath;
      this.client = client;
      this.longPollingClient = longPollingClient;
   }

   /**
    * Create new instance running at specified URL
    * 
    * @param nodeUrl the URL of the node
    * @param apiPath the path to the API
    */
   public ServiceBase(URL nodeUrl, String apiPath) {
      this(nodeUrl, apiPath, createClient(30), createClient(0));
   }

   /**
    * create new http client with required initialization
    * 
    * @param readTimeoutSeconds number of seconds of read timeout. 0 for no timeout
    * @return initialized http client
    */
   protected static OkHttpClient createClient(long readTimeoutSeconds) {
      return new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(Level.BASIC))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
   }

   /**
    * Create new instance running at specified URL with default API path
    * 
    * @param nodeUrl the URL of the node
    */
   public ServiceBase(URL nodeUrl) {
      this(nodeUrl, API_PATH);
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

   /**
    * @return the client
    */
   protected OkHttpClient getClient() {
      return client;
   }

   /**
    * @return the longPollingClient
    */
   protected OkHttpClient getLongPollingClient() {
      return longPollingClient;
   }
   
   
}
