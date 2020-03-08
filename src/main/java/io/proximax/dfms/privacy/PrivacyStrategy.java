package io.proximax.dfms.privacy;

import java.io.InputStream;

import io.proximax.dfms.privacy.strategy.CustomPrivacyStrategy;

/**
 * The privacy strategy interface<br>
 * <br>
 * Privacy strategy handles the encrypting and decrypting of data <br>
 * <br>
 * When creating a custom Privacy Strategy, implement CustomPrivacyStrategy
 * 
 * @see CustomPrivacyStrategy
 */
public interface PrivacyStrategy {

   /**
    * Get the privacy type's int value
    * 
    * @return the privacy type's int value
    */
   int getPrivacyType();

   /**
    * Encrypt byte stream
    * 
    * @param stream the byte stream to encrypt
    * @return encrypted byte stream
    */
   InputStream encryptStream(final InputStream stream);

   /**
    * Encrypt byte stream
    * 
    * @param encryptedStream the byte stream to decrypt
    * @return the decrypted data
    */
   InputStream decryptStream(final InputStream encryptedStream);
}
