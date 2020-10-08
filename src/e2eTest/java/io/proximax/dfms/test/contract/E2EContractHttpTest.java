/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.dfms.ContractClientServices;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.contract.Contract;

/**
 * integration tests for contract end-points
 */
class E2EContractHttpTest {
   private static final Cid CONTRACT = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");

   DFMSClient api;

   @BeforeEach
   void init() throws MalformedURLException {
      api = new DFMSClient(new URL("http://localhost:6366"));
   }

   @Test
   void listContracts() {
      ContractClientServices contracts = api.createContractClientServices();
      // list contracts on the api node
      List<Cid> listedCids = contracts.list().timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertTrue(listedCids.contains(CONTRACT));
   }

   @Test
   void getContract() throws IOException {
      ContractClientServices contracts = api.createContractClientServices();
      // retrieve info by contract id
      Contract contr = contracts.get(CONTRACT).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertEquals(CONTRACT, contr.getId());
   }

}
