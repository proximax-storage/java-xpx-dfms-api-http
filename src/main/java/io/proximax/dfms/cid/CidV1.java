/**
 * 
 */
package io.proximax.dfms.cid;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import io.proximax.dfms.cid.multibase.Multibase;
import io.proximax.dfms.cid.multihash.Multihash;

/**
 * Cid version 1 introduces header in-front of multihash. This header identifies multihash version and codec - each as
 * an unsigned byte
 */
public class CidV1 implements Cid {
   private static final int VERSION = 1;
   private static final int MAX_VARINT_LEN64 = 10;

   private final CidCodec codec;
   private final Multihash hash;

   /**
    * create new instance
    * 
    * @param codec the codec to use for this Cid
    * @param hash multihash representing the actual ID
    */
   public CidV1(CidCodec codec, Multihash hash) {
      this.codec = codec;
      this.hash = hash;
   }

   /**
    * @return the codec
    */
   public CidCodec getCodec() {
      return codec;
   }

   @Override
   public Multihash getHash() {
      return hash;
   }

   /**
    * serialize value into byte array
    * 
    * @return byte array representing this Cd
    */
   @Override
   public byte[] toBytes() {
      byte[] hashBytes = hash.toBytes();
      byte[] res = new byte[2 * MAX_VARINT_LEN64 + hashBytes.length];
      int index = putUvarint(res, 0, VERSION);
      index = putUvarint(res, index, codec.getCode());
      System.arraycopy(hashBytes, 0, res, index, hashBytes.length);
      return Arrays.copyOfRange(res, 0, index + hashBytes.length);
   }

   @Override
   public String toString() {
      return encode(Multibase.BASE_32);
   }

   /**
    * decode the Cid from the string value
    * 
    * @param value multibase string encoding the Cid
    * @return the Cid
    */
   public static CidV1 decode(String value) {
      return decode(Multibase.decode(value));
   }

   /**
    * decode Cid from the byte array
    * 
    * @param data
    * @return
    */
   public static CidV1 decode(byte[] data) {
      InputStream in = new ByteArrayInputStream(data);
      try {
         long version = readVarint(in);
         if (version != 1) {
            throw new CidEncodingException("Invalid Cid version number: " + version);
         }
         CidCodec codec = CidCodec.getByCode(readVarint(in));
         Multihash hash = Multihash.deserialize(new DataInputStream(in));
         return new CidV1(codec, hash);
      } catch (IOException e) {
         // convert byte array to hexadecimal string
         String bytes = Stream.of(data).map(b -> String.format("%02x", b)).reduce("", (a, b) -> a + b);
         throw new CidEncodingException("Invalid cid bytes: " + bytes, e);
      }
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((codec == null) ? 0 : codec.hashCode());
      result = prime * result + ((hash == null) ? 0 : hash.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      CidV1 other = (CidV1) obj;
      if (codec != other.codec)
         return false;
      if (hash == null) {
         if (other.hash != null)
            return false;
      } else if (!hash.equals(other.hash))
         return false;
      return true;
   }

   /**
    * read variable byte encoded integer
    * 
    * @param in input stream with data
    * @return long value
    * @throws IOException when read failed or reached end of stream too early
    */
   private static long readVarint(InputStream in) throws IOException {
      long x = 0;
      int s = 0;
      for (int i = 0; i < 10; i++) {
         int b = in.read();
         if (b == -1)
            throw new EOFException();
         if (b < 0x80) {
            if (i > 9 || i == 9 && b > 1) {
               throw new IllegalStateException("Overflow reading varint" + (-(i + 1)));
            }
            return x | (((long) b) << s);
         }
         x |= ((long) b & 0x7f) << s;
         s += 7;
      }
      throw new IllegalStateException("Varint too long!");
   }

   /**
    * encode number into variable length and write it to provided buffer
    * 
    * @param buf the buffer to write to
    * @param index position where to write the value
    * @param x the value to encode and write to buffer
    * @return index of next available byte for write
    */
   private static int putUvarint(byte[] buf, int index, long x) {
      while (x >= 0x80) {
         buf[index] = (byte) (x | 0x80);
         x >>= 7;
         index++;
      }
      buf[index] = (byte) x;
      return index + 1;
   }
}
