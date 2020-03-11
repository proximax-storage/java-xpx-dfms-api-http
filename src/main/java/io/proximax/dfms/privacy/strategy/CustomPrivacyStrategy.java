package io.proximax.dfms.privacy.strategy;

import io.proximax.dfms.privacy.PrivacyStrategy;
import io.proximax.dfms.privacy.PrivacyType;

/**
 * The abstract class to be used when creating custom privacy strategy
 * <br>
 * <br>
 * This fixes the privacy type as CUSTOM
 * @see PrivacyType
 */
public abstract class CustomPrivacyStrategy implements PrivacyStrategy {

    /**
     * Get the privacy type which is set as CUSTOM
     * @return the privacy type's int value
     * @see PrivacyType
     */
    @Override
    public int getPrivacyType() {
        return PrivacyType.CUSTOM.getValue();
    }
}
