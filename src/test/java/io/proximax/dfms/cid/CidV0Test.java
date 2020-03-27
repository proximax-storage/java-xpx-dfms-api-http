/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.cid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.cid.multibase.Multibase.Base;
import io.proximax.dfms.cid.multihash.MultihashType;


/**
 * {@link Cid} tests
 */
class CidV0Test {

   @Test
   void testDecode() {
      System.out.println("QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn".length());
      assertEquals(MultihashType.ID, CidV0.decode("QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn").getHash().getType());
   }

   @Test
   void testEncode() {
      Cid cid = CidV0.decode("QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn");
      assertEquals("bAARBEICZSSCDSBS7FFQZ55ASQDF3SMV6KLCW3GOFSZVWLYARCI47BGF354======", cid.encode(Base.Base32));
   }

}
