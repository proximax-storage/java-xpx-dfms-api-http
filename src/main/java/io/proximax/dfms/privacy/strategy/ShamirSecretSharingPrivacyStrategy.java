package io.proximax.dfms.privacy.strategy;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;

import com.codahale.shamir.Scheme;

import io.proximax.cipher.PBECipherEncryptor;
import io.proximax.dfms.model.PrivacyType;

/**
 * The privacy strategy that secures the data using shamir secret sharing. <br>
 * <br>
 * This strategy requires the total count of secret parts, minimum count of parts to build secret, and the secret parts.
 */
public final class ShamirSecretSharingPrivacyStrategy extends PrivacyStrategy {

   private final char[] secret;
   private final PBECipherEncryptor pbeCipherEncryptor;

   ShamirSecretSharingPrivacyStrategy(PBECipherEncryptor pbeCipherEncryptor, int secretTotalPartCount,
         int secretMinimumPartCountToBuild, Map<Integer, byte[]> secretParts) {

      Validate.isTrue(secretTotalPartCount > 0, "secretTotalPartCount should be a positive number");
      Validate.isTrue(secretMinimumPartCountToBuild > 0 && secretMinimumPartCountToBuild <= secretTotalPartCount,
            "secretMinimumPartCountToBuild should be a positive number less than or equal to secretTotalPartCount");
      Validate.notNull(secretParts, "secretParts is required");
      Validate.isTrue(secretParts.size() >= secretMinimumPartCountToBuild,
            "secretParts should meet minimum part count as defined by secretMinimumPartCountToBuild");

      this.secret = new String(Scheme.of(secretTotalPartCount, secretMinimumPartCountToBuild).join(secretParts))
            .toCharArray();
      this.pbeCipherEncryptor = pbeCipherEncryptor;
   }

   /**
    * Get the privacy type which is set as SHAMIR
    * 
    * @return the privacy type's int value
    * @see PrivacyType
    */
   @Override
   public int getPrivacyType() {
      return PrivacyType.SHAMIR.getValue();
   }

   /**
    * Encrypt byte stream using the shamir secret sharing
    * 
    * @param stream the byte stream to encrypt
    * @return the encrypted byte stream
    */
   @Override
   public final InputStream encryptStream(final InputStream stream) {
      return pbeCipherEncryptor.encryptStream(stream, secret);
   }

   /**
    * Decrypt byte stream using the shamir secret sharing
    * 
    * @param encryptedStream the byte stream to decrypt
    * @return the decrypted byte stream
    */
   @Override
   public final InputStream decryptStream(final InputStream encryptedStream) {
      return pbeCipherEncryptor.decryptStream(encryptedStream, secret);
   }

   /**
    * A model class to represent a secret part which is composed of index and the secret part data
    */
   public static class SecretPart {

      private final int index;
      private final byte[] data;

      /**
       * Construct instance of this model
       * 
       * @param index the index of the secret part
       * @param data the data of the secret part
       */
      public SecretPart(int index, byte[] data) {
         this.index = index;
         this.data = data;
      }

      /**
       * Construct instance of this model
       * 
       * @param index the index of the secret part
       * @param secretPart the data of the secret part
       * @return instance of this model
       */
      public static SecretPart secretPart(int index, byte[] secretPart) {
         return new SecretPart(index, secretPart);
      }
   }

   /**
    * Create instance of this strategy using an array of secret parts
    * 
    * @param secretTotalPartCount the total count of secret parts
    * @param secretMinimumPartCountToBuild the minimum count of parts to build secret
    * @param secretParts array of secret parts
    * @return the instance of this strategy
    */
   public static ShamirSecretSharingPrivacyStrategy create(int secretTotalPartCount, int secretMinimumPartCountToBuild,
         SecretPart... secretParts) {
      return new ShamirSecretSharingPrivacyStrategy(new PBECipherEncryptor(), secretTotalPartCount,
            secretMinimumPartCountToBuild,
            Stream.of(secretParts).collect(Collectors.toMap(parts -> parts.index, parts -> parts.data)));
   }

   /**
    * Create instance of this strategy using a list of secret parts
    * 
    * @param secretTotalPartCount the total count of secret parts
    * @param secretMinimumPartCountToBuild the minimum count of parts to build secret
    * @param secretParts list of secret parts
    * @return the instance of this strategy
    */
   public static ShamirSecretSharingPrivacyStrategy create(int secretTotalPartCount, int secretMinimumPartCountToBuild,
         List<SecretPart> secretParts) {
      return new ShamirSecretSharingPrivacyStrategy(new PBECipherEncryptor(), secretTotalPartCount,
            secretMinimumPartCountToBuild, secretParts == null ? Collections.emptyMap()
                  : secretParts.stream().collect(Collectors.toMap(parts -> parts.index, parts -> parts.data)));
   }

   /**
    * Create instance of this strategy using a map of secret parts
    * 
    * @param secretTotalPartCount the total count of secret parts
    * @param secretMinimumPartCountToBuild the minimum count of parts to build secret
    * @param secretParts map of secret parts
    * @return the instance of this strategy
    */
   public static ShamirSecretSharingPrivacyStrategy create(int secretTotalPartCount, int secretMinimumPartCountToBuild,
         Map<Integer, byte[]> secretParts) {
      return new ShamirSecretSharingPrivacyStrategy(new PBECipherEncryptor(), secretTotalPartCount,
            secretMinimumPartCountToBuild, secretParts == null ? Collections.emptyMap() : secretParts);
   }

}
