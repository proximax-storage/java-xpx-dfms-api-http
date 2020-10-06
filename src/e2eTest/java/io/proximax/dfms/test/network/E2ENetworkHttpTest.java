/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.libp2p.core.PeerId;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.NetworkRepository;

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
      NetworkRepository repo = api.createNetworkRepository();
      // list contracts on the api node
      PeerId id = repo.getId().blockingFirst();
      assertNotNull(id);
      assertEquals("12D3L7AV8AxKhLz5DzMfN91TBwJBMXLXuiTTfw5R9V3FLyWCfqdo", id.toBase58());
      assertEquals("00240804122092d3b9df67c08333716c880be295d64dcecbed0ce1a725faf4e8be6dc45e2e29", id.toHex());
   }
}
