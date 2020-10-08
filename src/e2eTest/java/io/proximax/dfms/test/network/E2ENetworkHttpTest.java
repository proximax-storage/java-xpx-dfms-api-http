/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.network;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.libp2p.core.PeerId;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.NetworkServices;

/**
 * TODO add proper description
 */
class E2ENetworkHttpTest {

   DFMSClient api;

   @BeforeEach
   void init() throws MalformedURLException {
      api = new DFMSClient(new URL("http://localhost:6366"));
   }

   @Test
   void getId() {
      NetworkServices repo = api.createNetworkServices();
      // list contracts on the api node
      PeerId id = repo.getId().blockingFirst();
      assertNotNull(id);
   }
}
