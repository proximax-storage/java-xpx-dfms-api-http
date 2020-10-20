/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.contract;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import io.proximax.dfms.gen.model.Contract;

/**
 * TODO add proper description
 */
class DriveContractTest {

   @Test
   void test() {
      Contract dto = new Gson().fromJson("{\"DriveContract\": {}}", Contract.class);
      assertNotNull(dto);
   }

}
