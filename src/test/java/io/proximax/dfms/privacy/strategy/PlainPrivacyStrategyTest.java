package io.proximax.dfms.privacy.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.dfms.privacy.PrivacyType;

public class PlainPrivacyStrategyTest {

   private static final String FOX = "the quick brown fox jumps over the lazy dog";
   
   private PlainPrivacyStrategy unitUnderTest;

   @BeforeEach
   public void setUp() {
      unitUnderTest = PlainPrivacyStrategy.create();
   }

   @Test
   public void shouldReturnCorrectPrivacyType() {
      final int privacyType = unitUnderTest.getPrivacyType();

      assertEquals(PrivacyType.PLAIN.getValue(), privacyType);
   }

   @Test
   public void shouldReturnSameDataOnEncrypt() {
      final InputStream dataStream = new ByteArrayInputStream(FOX.getBytes());

      final InputStream encrypted = unitUnderTest.encryptStream(dataStream);

      // assert stream object equality
      assertEquals(dataStream, encrypted);
   }

   @Test
   public void shouldReturnSameDataOnDecrypt() {
      final InputStream dataStream = new ByteArrayInputStream(FOX.getBytes());
      final InputStream decrypted = unitUnderTest.decryptStream(dataStream);

      assertEquals(dataStream, decrypted);
   }
}
