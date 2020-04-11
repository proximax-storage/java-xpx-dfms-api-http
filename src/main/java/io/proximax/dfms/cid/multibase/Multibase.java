package io.proximax.dfms.cid.multibase;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Self-described BaseX encoding. First letter identifies which base is used.
 */
public enum Multibase {
   BASE_2('0', Base2::encode, Base2::decode), // binary has 1 and 0
   BASE_16('f', Base16::encode, Base16::decode), // highest char in hex
   BASE_32('b', Base32::encode, Base32::decode), // rfc4648 no padding
   BASE_58_BTC('z', Base58::encode, Base58::decode); // highest char

   private final char prefix;
   private final Function<byte[], String> encoder;
   private final Function<String, byte[]> decoder;
   
   // lookup table for quick retrieval of multibase type
   private static Map<Character, Multibase> lookup = new TreeMap<>();
   static {
      for (Multibase b : Multibase.values())
         lookup.put(b.prefix, b);
   }

   /**
    * create new instance of enum
    * 
    * @param prefix first character of encoded value
    * @param encoder function that performs encoding
    * @param decoder function that performs decoding
    */
   private Multibase(char prefix, Function<byte[], String> encoder, Function<String, byte[]> decoder) {
      this.prefix = prefix;
      this.encoder = encoder;
      this.decoder = decoder;
   }

   /**
    * @return the prefix
    */
   public char getPrefix() {
      return prefix;
   }

   /**
    * @return the encoder
    */
   public Function<byte[], String> getEncoder() {
      return encoder;
   }

   /**
    * @return the decoder
    */
   public Function<String, byte[]> getDecoder() {
      return decoder;
   }

   /**
    * encode the data to a String representation. First character will be prefix to identify encoding
    * 
    * @param data array of bytes
    * @return encoded String
    */
   public String encode(final byte[] data) {
      return getPrefix() + getEncoder().apply(data);
   }

   /**
    * decode the string data to byte array. Uses first character to identify use encoding
    * 
    * @param data encoded string
    * @return decoded byte array
    */
   public static byte[] decode(final String data) {
      Multibase base = getByCode(data.charAt(0));
      return base.getDecoder().apply(data.substring(1));
   }

   /**
    * get multibase type by the code
    * 
    * @param code the code of the multibase type (the first character)
    * @return the enum item
    */
   public static Multibase getByCode(char code) {
      if (!lookup.containsKey(code)) {
         throw new IllegalStateException("Unknown Multibase code: " + code);
      }
      return lookup.get(code);
   }
}
