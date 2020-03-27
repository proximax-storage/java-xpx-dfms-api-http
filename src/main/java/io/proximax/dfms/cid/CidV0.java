/**
 * 
 */
package io.proximax.dfms.cid;

import org.apache.commons.lang3.Validate;

import io.proximax.dfms.cid.multibase.Base58;
import io.proximax.dfms.cid.multihash.Multihash;
import io.proximax.dfms.cid.multihash.MultihashType;

/**
 * Cid version 0 is plain multihash
 */
public class CidV0 implements Cid {

   private final Multihash hash;

   /**
    * new instance from the multihash
    * 
    * @param hash the identity hash
    */
   public CidV0(Multihash hash) {
      Validate.notNull(hash, "hash is mandatory");
      this.hash = hash;
   }

   @Override
   public Multihash getHash() {
      return hash;
   }

   @Override
   public byte[] toBytes() {
      return hash.toBytes();
   }

   /**
    * decode cid from the string representation
    * 
    * @param value Base58 encoded hash
    * @return new instance
    */
   public static CidV0 decode(String value) {
      return new CidV0(new Multihash(MultihashType.ID, Base58.decode(value)));
   }
}
