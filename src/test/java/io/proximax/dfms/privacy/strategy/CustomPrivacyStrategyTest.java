package io.proximax.dfms.privacy.strategy;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.privacy.PrivacyType;
import io.proximax.dfms.privacy.strategy.CustomPrivacyStrategy;


public class CustomPrivacyStrategyTest {

    @Test
    public void shouldHaveCustomPrivacyType() {
        final CustomPrivacyStrategy result = new CustomPrivacyStrategy() {

            @Override
            public InputStream encryptStream(InputStream stream) {
                return null;
            }

            @Override
            public InputStream decryptStream(InputStream encryptedStream) {
                return null;
            }
        };

        assertEquals(PrivacyType.CUSTOM.getValue(), result.getPrivacyType());
    }
}
