/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.cid;

import org.junit.jupiter.api.Test;

import io.ipfs.cid.Cid;

/**
 * TODO add proper description
 */
class CidTest {

   @Test
   void test1() {
      Cid.decode("QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn");
   }

   @Test
   void test2() {
      Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
   }

}
