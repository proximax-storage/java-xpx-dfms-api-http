/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.Cid;
import io.proximax.dfms.ContractRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.contract.Contract;

/**
 * TODO add proper description
 */
class E2EContractHttpTest {

   private static final Cid FIXED_CID = Cid
         .decode("08041220720a6dc299b7672e623cef70f9f6f827d81ea4b1d1f545854422a77502f10090");

   StorageApi api;

   @BeforeEach
   void init() throws MalformedURLException {
      api = new StorageApi(new URL("http://localhost:6366"));
   }

   @Test
   void listContracts() {
      ContractRepository contracts = api.createContractRepository();
      // list contracts on the api node
      List<Cid> listedCids = contracts.list().timeout(30, TimeUnit.SECONDS).toList().blockingGet();
      assertEquals(1, listedCids.size());
      assertEquals(FIXED_CID, listedCids.get(0));
   }

   @Test
   void getContract() throws IOException {
      ContractRepository contracts = api.createContractRepository();
      // retrieve info by contract id
      Contract contr = ((ContractHttp) contracts).get(FIXED_CID).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertEquals(FIXED_CID, contr.getCid());
      assertEquals("0804122035f3f497d7d49ce76a1637615be4cb598bd412ca03bbddb525a04779e346f22f", contr.getOwner());
   }
}
