package io.proximax.dfms.cid;

import org.apache.commons.lang3.Validate;

import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.cid.multibase.Multibase;
import io.proximax.dfms.cid.multihash.Multihash;
import io.proximax.dfms.cid.multihash.MultihashType;

/**
 * self-described identifier
 */
public interface Cid {

   /**
    * serialize Cid to byte array
    * 
    * @return byte arrays with serialized Cid
    */
   byte[] toBytes();

   /**
    * get the hash representing the identity
    * 
    * @return the multihash instance
    */
   Multihash getHash();
   
   /**
    * encode Cid to string using specified encoding
    * 
    * @param base encoding to use
    * @return encoded string
    */
   default String encode(Multibase base) {
      return base.encode(toBytes());
   }

   /**
    * decode the Cid from string
    * 
    * @param cidString string representation of Cid
    * @return the Cid instance
    */
   static Cid decode(String cidString) {
      Validate.notNull(cidString, "Can not decode CID from null");
      if (cidString.startsWith("Q") && cidString.length() == 46) {
         // CidV0 is Base58 encoded (starts by Q) and is 46 character long
         return CidV0.decode(cidString);
      } else {
         // CidV1
         return CidV1.decode(cidString);
      }
   }

   /**
    * create Cid from public key
    * 
    * @param publicKey public key representing an identity
    * @return Cid instance
    */
   static Cid fromPublicKey(PublicKey publicKey) {
      return new CidV1(CidCodec.PROXIMA_X, new Multihash(MultihashType.ID, publicKey.getRaw()));
   }
}