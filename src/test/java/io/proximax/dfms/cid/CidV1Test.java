/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.cid;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.cid.multibase.Base32;
import io.proximax.dfms.cid.multibase.Multibase.Base;
import io.proximax.dfms.cid.multihash.MultihashType;

/**
 * {@link Cid} tests
 */
class CidV1Test {

   @Test
   void testBase32EncodedCid() {
      assertEquals(MultihashType.SHA2_256,
            CidV1.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu").getHash().getType());
   }

   @Test
   void testBase58EncodedCid() {
      assertEquals(MultihashType.SHA2_256,
            CidV1.decode("zdpuAyvkgEDQm9TenwGkd5eNaosSxjgEYd8QatfPetgB1CdEZ").getHash().getType());
   }

   @Test
   void testBase32() {
      String base = "aegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu";
      byte[] multibaseBytes = Base32.decode(base);
      byte[] apacheBytes = new org.apache.commons.codec.binary.Base32().decode(base);
      assertArrayEquals(apacheBytes, multibaseBytes);
   }
   
   @Test
   void testEncode() {
      Cid cid = CidV1.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
      assertEquals("bAEGBEIBONDKKRHXFPRZWRLGXXLTAVQHWEH2YLHU4HGO5LXJXPQBPFSW2LU======", cid.encode(Base.Base32));
   }
}
