/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.ipfs.cid.Cid;
import io.ipfs.multihash.Multihash;
import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.drive.InputStreamContent;

/**
 * TODO add proper description
 */
class E2EDriveHttpTest {

   @Test
   void test() throws IOException {
      StorageApi api = new StorageApi(new URL("http://54.169.42.125"));
      DriveRepository drive = api.createDriveRepository();
      Cid cid = drive.add(new Cid(Multihash.fromBase58("QmatmE9msSfkKxoffpHwNLNKgwZG8eT9Bud6YoPab52vpy")),
            "hello",
            new InputStreamContent(Optional.of("ahoj"),
                  E2EDriveHttpTest.class.getClassLoader().getResourceAsStream("data.txt"))).blockingFirst();
      System.out.println(cid);
   }

}
