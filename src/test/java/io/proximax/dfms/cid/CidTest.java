/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.cid;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.cid.Cid.Codec;
import io.proximax.dfms.cid.multibase.Base32;
import io.proximax.dfms.cid.multihash.Multihash;
import io.proximax.dfms.cid.multihash.Multihash.Type;


/**
 * TODO add proper description
 */
class CidTest {

   @Test
   void testLegacyFormat() {
      assertNotNull(Cid.decode("QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn"));
   }

   @Test
   void testBase32EncodedCid() {
      assertNotNull(Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu"));
   }

   @Test
   void testBase58EncodedCid() {
      assertNotNull(Cid.decode("zdpuAyvkgEDQm9TenwGkd5eNaosSxjgEYd8QatfPetgB1CdEZ"));
   }
   
   @Test
   void testBase32() {
      String base = "aegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu";
      byte[] multibaseBytes = Base32.decode(base);
      byte[] apacheBytes = new org.apache.commons.codec.binary.Base32().decode(base);
      assertArrayEquals(apacheBytes, multibaseBytes);
   }
   
   @Test
   void testConversions() {
      final String pubKeyStr = "08011220201b155bf3ebe4dcca522549a9835a21d010d07e6c354f0df30a0a0504b83f1b";
      PublicKey pubKey = PublicKey.fromHexString(pubKeyStr);
      Cid cid = cidFromPublicKey(pubKey);
      assertEquals(Cid.decode("bAEGAAJAIAEJCAIA3CVN7H27E3TFFEJKJVGBVUIOQCDIH43BVJ4G7GCQKAUCLQPY3"), cid);
   }
   
   public static Cid cidFromPublicKey(PublicKey publicKey) {
      return Cid.buildCidV1(Codec.PROXIMA_X, Type.id, publicKey.getRaw());
   }
}
