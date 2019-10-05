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

/**
 * TODO add proper description
 */
class StorageApiTest {
   private static final String REPLICATOR_URL = "http://54.179.139.207";
   @Test
   void testUrl() throws MalformedURLException {
      StorageApi api = new StorageApi(new URL(REPLICATOR_URL));
      
      assertEquals(REPLICATOR_URL, api.getUrl().toExternalForm());
   }

}
