/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.net.URL;

import io.proximax.dfms.http.repos.ContractHttp;
import io.proximax.dfms.http.repos.DriveHttp;

/**
 * Central access point to the storage API. Provides factories for DFMS service repositories
 */
public class StorageApi implements ServiceNode {
   
   public static final String API_PATH = "/api/v1";
   
   /** URL of the node */
   private final URL nodeUrl;
   /** the path to the API on the node */
   private final String apiPath;

   /**
    * Create new instance running at specified URL
    * 
    * @param nodeUrl the URL of the node
    * @param apiPath the path to the API
    */
   public StorageApi(URL nodeUrl, String apiPath) {
      this.nodeUrl = nodeUrl;
      this.apiPath = apiPath;
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
      return new DriveHttp(this, apiPath);
   }
   
   /**
    * create contract repository
    * 
    * @return new instance
    */
   public ContractRepository createContractRepository() {
      return new ContractHttp(this, apiPath);
   }

   @Override
   public URL getUrl() {
      return nodeUrl;
   }
   
}
