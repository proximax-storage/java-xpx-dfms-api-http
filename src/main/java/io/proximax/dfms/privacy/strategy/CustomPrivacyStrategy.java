package io.proximax.dfms.privacy.strategy;

import io.proximax.dfms.model.PrivacyType;

/**
 * The abstract class to be used when creating custom privacy strategy
 * <br>
 * <br>
 * This fixes the privacy type as CUSTOM
 * @see PrivacyType
 */
public abstract class CustomPrivacyStrategy extends PrivacyStrategy {

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
