/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.http.repos.ContractHttp;
import io.proximax.dfms.http.repos.DriveHttp;
import io.proximax.dfms.http.repos.NetworkHttp;

/**
 * TODO add proper description
 */
class StorageApiTest {
   private static final String REPLICATOR_URL = "http://1.2.3.4:5678";
   
   @Test
   void testUrl() throws MalformedURLException {
      StorageApi api = new StorageApi(new URL(REPLICATOR_URL));
      
      assertEquals(REPLICATOR_URL, api.getUrl().toExternalForm());
      assertEquals(StorageApi.API_PATH, api.getApiPath());
   }

   @Test
   void testCustomApiPath() throws MalformedURLException {
      final String apiPath = "test/v7";
      StorageApi api = new StorageApi(new URL(REPLICATOR_URL), apiPath);
      
      assertEquals(REPLICATOR_URL, api.getUrl().toExternalForm());
      assertEquals(apiPath, api.getApiPath());
   }

   @SuppressWarnings("unchecked")
   @Test
   void testContractRepo() throws MalformedURLException {
      final String apiPath = "test/v7";
      StorageApi api = new StorageApi(new URL(REPLICATOR_URL), apiPath);
      
      ContractHttp contractRepo = ((ContractHttp)api.createContractRepository());
      assertEquals("http://1.2.3.4:5678/test/v7", contractRepo.getApiUrl().toString());
      assertEquals(api, contractRepo.getApi());
   }
   
   @SuppressWarnings("unchecked")
   @Test
   void testDriveRepo() throws MalformedURLException {
      final String apiPath = "test/v7";
      StorageApi api = new StorageApi(new URL(REPLICATOR_URL), apiPath);
      
      DriveHttp driveRepo = ((DriveHttp)api.createDriveRepository());
      assertEquals("http://1.2.3.4:5678/test/v7", driveRepo.getApiUrl().toString());
      assertEquals(api, driveRepo.getApi());
   }
   
   @SuppressWarnings("unchecked")
   @Test
   void testNetworkRepo() throws MalformedURLException {
      final String apiPath = "test/v7";
      StorageApi api = new StorageApi(new URL(REPLICATOR_URL), apiPath);
      
      NetworkHttp netRepo = ((NetworkHttp)api.createNetworkRepository());
      assertEquals("http://1.2.3.4:5678/test/v7", netRepo.getApiUrl().toString());
      assertEquals(api, netRepo.getApi());
   }
}
