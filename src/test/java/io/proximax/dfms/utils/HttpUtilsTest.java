/**
 * 
 */
package io.proximax.dfms.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.cid.Cid;

/**
 * {@link HttpUtils} tests
 */
class HttpUtilsTest {
   private static final String BTC_ID = "zTkPYhLvEBkjMoAFKCrL8bNKEeFJRNEEugJ1EdTyadvyryhp8";
   private static final Cid CONTRACT = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
   private static final Cid CONTRACT_BTC = Cid.decode(BTC_ID);

   @Test
   void testUrlEncode() {
      assertEquals("test+word", HttpUtils.encode("test word"));
   }

   @Test
   void testEncodeCid() {
      assertEquals(BTC_ID, HttpUtils.encode(CONTRACT));
      assertEquals(BTC_ID, HttpUtils.encode(CONTRACT_BTC));
   }
}
