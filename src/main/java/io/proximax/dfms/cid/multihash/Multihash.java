package io.proximax.dfms.cid.multihash;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.Validate;

import io.proximax.dfms.cid.multibase.Base58;

/**
 * Self-described hash. First 2 bytes represent type of the hashing function used ( {@link MultihashType} ) and length
 * of the hashed data in bytes
 * 
 * @see <a href="https://github.com/multiformats/multihash">Multihash specification</a>
 */
public class Multihash {

   private final MultihashType type;
   private final byte[] hash;

   /**
    * <p>
    * create new Multihash instance specifying type and hash bytes.
    * </p>
    * <p>
    * <b>NOTICE</b> that hash byte array is internally used and even though this object is expected to be immutable it
    * can be changed if the array is changed externally
    * </p>
    * 
    * @param type type of the hashing algorithm
    * @param hash byte array representing the hash data
    */
   public Multihash(final MultihashType type, final byte[] hash) {
      Validate.inclusiveBetween(type.getMinLength(),
            type.getMaxLength(),
            hash.length,
            "Hash with length %d is not valid for hash type %s",
            hash.length,
            type);
      // make assignments
      this.type = type;
      this.hash = hash;
   }

   /**
    * Create copy of original multihash instance. Underlying byte array with hash data is reused with new instance
    * 
    * @param original the original instance used as source of data
    */
   public static Multihash create(final Multihash original) {
      return new Multihash(original.type, original.hash);
   }

   /**
    * create new multihash instance from serialized bytes of another multihash instance. This assumes first byte is hash
    * type index and second byte is length
    * 
    * @param multihashBytes byte array with serialized multihash instance
    * @return multihash instance based on parameter
    */
   public static Multihash create(final byte[] multihashBytes) {
      // take first byte for index
      int index = multihashBytes[0] & 0xff;
      // ignore second byte (size) and read all remaining bytes as hash data
      byte[] hashBytes = Arrays.copyOfRange(multihashBytes, 2, multihashBytes.length);
      // create new instance
      return new Multihash(MultihashType.getByIndex(index), hashBytes);
   }

   /**
    * serialize this multihash instance to byte array
    * 
    * @return byte array representing this multihash instance
    */
   public byte[] toBytes() {
      // result will have extra 2 bytes for index and length
      byte[] multihashBytes = new byte[hash.length + 2];
      // write index and length on first 2 bytes
      multihashBytes[0] = (byte) type.getIndex();
      multihashBytes[1] = (byte) hash.length;
      // write hash to remaining bytes
      System.arraycopy(hash, 0, multihashBytes, 2, hash.length);
      // return the result
      return multihashBytes;
   }

   /**
    * @return the type of the hashing function used for this multihash
    */
   public MultihashType getType() {
      return type;
   }

   /**
    * hash bytes. Makes copy of the byte array to prevent external modification as {@link Multihash} is considered
    * immutable
    * 
    * @return copy of the hash byte array
    */
   public byte[] getHash() {
      return Arrays.copyOf(hash, hash.length);
   }

   /**
    * serialize this multihash into {@link DataOutput}. Multihash i serialized into array and all bytes are written at
    * once. This should be fine as multihash is fairly small
    * 
    * @param dout
    * @throws IOException
    */
   public void serialize(final DataOutput dout) throws IOException {
      dout.write(toBytes());
   }

   /**
    * deserialize the multihash from the {@link DataInput}
    * 
    * @param din
    * @return
    * @throws IOException
    */
   public static Multihash deserialize(final DataInput din) throws IOException {
      // read type index and length
      int typeIndex = din.readUnsignedByte();
      int len = din.readUnsignedByte();
      // lookup the type
      MultihashType type = MultihashType.getByIndex(typeIndex);
      // read the hash data
      byte[] hash = new byte[len];
      din.readFully(hash);
      // return instance
      return new Multihash(type, hash);
   }

   @Override
   public String toString() {
      // Base58 is used for toString
      return Base58.encode(toBytes());
   }

   @Override
   public boolean equals(final Object o) {
      if (!(o instanceof Multihash))
         return false;
      return type == ((Multihash) o).type && Arrays.equals(hash, ((Multihash) o).hash);
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(hash) ^ type.hashCode();
   }
}