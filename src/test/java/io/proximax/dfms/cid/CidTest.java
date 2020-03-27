/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.cid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.PublicKey;

/**
 * {@link Cid} tests
 */
class CidTest {

   @Test
   void testLegacyFormatV0() {
      assertNotNull(Cid.decode("QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn"));
   }

   @Test
   void testBase32EncodedCidV1() {
      assertNotNull(Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu"));
   }

   @Test
   void testBase58EncodedCidV1() {
      assertNotNull(Cid.decode("zdpuAyvkgEDQm9TenwGkd5eNaosSxjgEYd8QatfPetgB1CdEZ"));
   }

   @Test
   void testConversions() {
      final String pubKeyStr = "08011220201b155bf3ebe4dcca522549a9835a21d010d07e6c354f0df30a0a0504b83f1b";
      PublicKey pubKey = PublicKey.fromHexString(pubKeyStr);
      Cid cid = Cid.fromPublicKey(pubKey);
      assertEquals(Cid.decode("bAEGAAJAIAEJCAIA3CVN7H27E3TFFEJKJVGBVUIOQCDIH43BVJ4G7GCQKAUCLQPY3"), cid);
   }
}
