package io.proximax.dfms.model;

import io.proximax.dfms.privacy.strategy.CustomPrivacyStrategy;
import io.proximax.dfms.privacy.strategy.NemKeysPrivacyStrategy;
import io.proximax.dfms.privacy.strategy.PasswordPrivacyStrategy;
import io.proximax.dfms.privacy.strategy.PlainPrivacyStrategy;
import io.proximax.dfms.privacy.strategy.ShamirSecretSharingPrivacyStrategy;

/**
 * Enumerates the different privacy types
 * @see io.proximax.dfms.privacy.strategy.PrivacyStrategy
 */
public enum PrivacyType {

    /**
     * The type for plain privacy strategy
     * @see PlainPrivacyStrategy
     */
    PLAIN(1001),
    /**
     * The type for secured with nem keys privacy strategy
     * @see NemKeysPrivacyStrategy
     */
    NEMKEYS(1002),
    /**
     * The type for secured with shamir secret sharing privacy strategy
     * @see ShamirSecretSharingPrivacyStrategy
     */
    SHAMIR(1003),
    /**
     * The type for secured with password privacy strategy
     * @see PasswordPrivacyStrategy
     */
    PASSWORD(1004),
    /**
     * The type for custom privacy strategy
     * @see CustomPrivacyStrategy
     */
    CUSTOM(2001);

    private final int value;

    private PrivacyType(int value) {
        this.value = value;
    }

    /**
     * Get the int value of the privacy type
     * @return the privacy type's int value
     */
    public int getValue() {
        return value;
    }
}
