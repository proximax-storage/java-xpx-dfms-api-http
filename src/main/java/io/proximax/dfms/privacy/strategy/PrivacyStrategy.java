package io.proximax.dfms.privacy.strategy;

import java.io.InputStream;

/**
 * The abstract class privacy strategy
 * <br>
 * <br>
 * Privacy strategy handles the encrypting and decrypting of data
 * <br>
 * <br>
 * When creating a custom Privacy Strategy, implement CustomPrivacyStrategy
 * @see CustomPrivacyStrategy
 */
public abstract class PrivacyStrategy {

    /**
     * Get the privacy type's int value
     * @return the privacy type's int value
     */
    public abstract int getPrivacyType();

    /**
     * Encrypt byte stream
     * @param stream the byte stream to encrypt
     * @return encrypted byte stream
     */
    public abstract InputStream encryptStream(final InputStream stream);

    /**
     * Encrypt byte stream
     * @param encryptedStream the byte stream to decrypt
     * @return the decrypted data
     */
    public abstract InputStream decryptStream(final InputStream encryptedStream);
}
