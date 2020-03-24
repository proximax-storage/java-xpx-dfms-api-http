/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.cid.Cid;
import io.proximax.dfms.ContractRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.model.contract.Contract;

/**
 * integration tests for contract endpoints
 */
class E2EContractHttpTest {

   private static final Cid CONTRACT = Cid
         .decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");

   StorageApi api;

   @BeforeEach
   void init() throws MalformedURLException {
      api = new StorageApi(new URL("http://localhost:6366"));
   }

   @Test
   void listContracts() {
      ContractRepository contracts = api.createContractRepository();
      // list contracts on the api node
      List<Cid> listedCids = contracts.list().timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertEquals(1, listedCids.size());
      assertEquals(CONTRACT, listedCids.get(0));
   }

   @Test
   void getContract() throws IOException {
      ContractRepository contracts = api.createContractRepository();
      // retrieve info by contract id
      Contract contr = contracts.get(CONTRACT).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertEquals(CONTRACT, contr.getId());
   }
}
