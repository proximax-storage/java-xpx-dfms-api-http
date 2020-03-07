package io.proximax.dfms.privacy.strategy;

import java.io.InputStream;

import io.proximax.dfms.model.PrivacyType;

/**
 * The plain privacy strategy.
 * <br>
 * <br>
 * This strategy does not encrypt nor decrypt the data.
 */
public final class PlainPrivacyStrategy extends PrivacyStrategy {

    private PlainPrivacyStrategy() {

    }

    /**
     * Get the privacy type which is set as PLAIN
     * @return the privacy type's int value
     * @see PrivacyType
     */
    @Override
    public int getPrivacyType() {
        return PrivacyType.PLAIN.getValue();
    }

    /**
     * Return same byte stream
     * @param stream the byte stream to encrypt
     * @return same byte stream
     */
    @Override
    public final InputStream encryptStream(final InputStream stream) {
        return stream;
    }

    /**
     * Return same byte stream
     * @param encryptedStream the byte stream to decrypt
     * @return same byte stream
     */
    @Override
    public final InputStream decryptStream(final InputStream encryptedStream) {
        return encryptedStream;
    }

    /**
     * Create instance of this strategy
     * @return the instance of this strategy
     */
    public static PlainPrivacyStrategy create() {
        return new PlainPrivacyStrategy();
    }
}
