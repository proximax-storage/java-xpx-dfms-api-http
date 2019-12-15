/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.cid;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import io.proximax.cid.multibase.Base32;


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

   @Test
   void test3() {
      io.ipfs.cid.Cid.decode("zdpuAyvkgEDQm9TenwGkd5eNaosSxjgEYd8QatfPetgB1CdEZ");
   }
   
   @Test
   void testBase32() {
      String base = "aegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu";
      byte[] multibaseBytes = Base32.decode(base);
      byte[] apacheBytes = new org.apache.commons.codec.binary.Base32().decode(base);
      assertArrayEquals(apacheBytes, multibaseBytes);
   }
}
