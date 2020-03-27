/**
 * 
 */
package io.proximax.dfms.cid;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;

/**
 * Available Cid codecs
 */
public enum CidCodec {
   PROXIMA_X(0x0C), RAW(0x55), DAG_PROTOBUFF(0x70), DAG_CBOR(0x71), ETHEREUM_BLOCK(0x90), ETHEREUM_TX(0x91),
   BITCOIN_BLOCK(0xb0), BITCOIN_TX(0xb1), ZCASH_BLOCK(0xc0), ZCASH_TX(0xc1);

   private final long code;

   /**
    * create new enum item
    * 
    * @param code the code representing given item
    */
   CidCodec(long code) {
      this.code = code;
   }

   /**
    * @return the code
    */
   public long getCode() {
      return code;
   }

   /** lookup table to retrieve enum items by their codes */
   private static Map<Long, CidCodec> lookup = new TreeMap<>();
   static {
      for (CidCodec c : CidCodec.values())
         lookup.put(c.code, c);
   }

   /**
    * get Cid codec by the code that represents it
    * 
    * @param code 
    * @return
    */
   public static CidCodec getByCode(long code) {
      CidCodec codec = lookup.get(code);
      Validate.notNull(codec, "Unknown Cid codec code %d", code);
      return codec;
   }
}
