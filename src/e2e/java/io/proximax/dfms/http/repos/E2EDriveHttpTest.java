/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.ipfs.cid.Cid;
import io.proximax.dfms.ContractRepository;
import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.contract.Contract;
import io.proximax.dfms.drive.InputStreamContent;

/**
 * TODO add proper description
 */
class E2EDriveHttpTest {

   @Test
   void test() throws IOException {
      StorageApi api = new StorageApi(new URL("http://54.179.139.207:?????"));
      // start off by composing contract
      ContractRepository contracts = api.createContractRepository();
      Contract contract = contracts.compose(BigInteger.valueOf(1000000), Duration.ofDays(30)).timeout(30, TimeUnit.SECONDS).blockingFirst();
      // now add something to the drive
      DriveRepository drive = api.createDriveRepository();
      Cid cid = drive.add(contract.getCid(),
            "hello",
            new InputStreamContent(Optional.of("ahoj"),
                  E2EDriveHttpTest.class.getClassLoader().getResourceAsStream("data.txt")))
            .timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
   }

}
