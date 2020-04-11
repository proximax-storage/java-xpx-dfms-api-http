/**
 * 
 */
package io.proximax.dfms.cid.multihash;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;

/**
 * Known/supported multihash types
 */
public enum MultihashType {
   /** ID type has length of up to 64 bytes */
   ID(0x00, 0, 64),
   // standard hash types with their indexes and lengths
   MD5(0xd5, 16), SHA1(0x11, 20), SHA2_256(0x12, 32), SHA2_512(0x13, 64), SHA3_224(0x17, 24), SHA3_256(0x16, 32),
   SHA3_512(0x14, 64), KECCAK_224(0x1a, 24), KECCAK_256(0x1b, 32), KECCAK_384(0x1c, 48), KECCAK_512(0x1d, 64),
   BLAKE2B(0x40, 64), BLAKE2S(0x41, 32);

   private final int index;
   private final int minLength;
   private final int maxLength;

   /**
    * create new enum instance
    * 
    * @param index index used to identify the multihash type
    * @param minLength expected minimal length of the hashed data in bytes
    * @param maxLength expected maximal length of the hashed data in bytes
    */
   MultihashType(final int index, final int minLength, final int maxLength) {
      this.index = index;
      this.minLength = minLength;
      this.maxLength = maxLength;
   }

   /**
    * create new enum instance for fixed hash length
    * 
    * @param index index used to identify the multihash type
    * @param length expected exact length of the hashed data in bytes
    */
   MultihashType(final int index, final int length) {
      this(index, length, length);
   }

   /**
    * @return the index
    */
   public int getIndex() {
      return index;
   }

   /**
    * @return the minLength
    */
   public int getMinLength() {
      return minLength;
   }

   /**
    * @return the maxLength
    */
   public int getMaxLength() {
      return maxLength;
   }

   /** lookup table for multihash types by index */
   private static Map<Integer, MultihashType> lookup = new TreeMap<>();
   static {
      // iterate over enum and
      for (MultihashType t : MultihashType.values())
         lookup.put(t.index, t);
   }

   /**
    * retrieve multihash type by index
    * 
    * @param index the index of requested multihash type
    * @return multihash type instance
    */
   public static MultihashType getByIndex(int index) {
      MultihashType type = lookup.get(index);
      Validate.notNull(type, "Unknown MultihashType index %d", index);
      return type;
   }

}
