package io.proximax.dfms.privacy.strategies;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.proximax.dfms.cipher.PBECipherEncryptor;
import io.proximax.dfms.privacy.PrivacyType;
import io.proximax.dfms.privacy.strategies.PasswordPrivacyStrategy;
import io.proximax.dfms.utils.PasswordUtils;

public class PasswordPrivacyStrategyTest {

   private static final byte[] SAMPLE_DATA = "the quick brown fox jumps over the lazy dog".getBytes();

   private static final String PASSWORD = "lkNzBmYmYyNTExZjZmNDYyZTdjYWJmNmY1MjJiYjFmZTk3Zjg2NDA5ZDlhOD";

   private static final String PASSWORD_TOO_SHORT = "too short";

   private static final int MIN_PASS_LEN = PasswordUtils.MINIMUM_PASSWORD_LENGTH;

   @Mock
   private PBECipherEncryptor encryptor;

   @BeforeEach
   public void setUp() {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void shouldReturnCorrectPrivacyType() {
      final int privacyType = PasswordPrivacyStrategy.create(PASSWORD).getPrivacyType();

      assertEquals(PrivacyType.PASSWORD.getValue(), privacyType);
   }

   @Test
   public void failInitWithoutPassword() {
      assertThrows(NullPointerException.class, () -> PasswordPrivacyStrategy.create(null));
   }

   @Test
   public void failInitWithPasswordNotMeetingMinimumLength() {
      assertThrows(IllegalArgumentException.class, () -> PasswordPrivacyStrategy.create(PASSWORD_TOO_SHORT));
   }

   @Test
   public void shouldCreateWithProvidedPassword() {
      final PasswordPrivacyStrategy strategy = PasswordPrivacyStrategy.create(PASSWORD);

      assertEquals(PASSWORD, strategy.getPassword());
   }

   @Test
   public void shouldCreateWithGeneratedPassword() {
      final PasswordPrivacyStrategy strategy = PasswordPrivacyStrategy.create();

      assertNotNull(strategy.getPassword());
      assertEquals(strategy.getPassword().length(), MIN_PASS_LEN);
   }

   @Test
   public void propagatesEncryptorEncException() {
      given(encryptor.encryptStream(Mockito.any(), Mockito.any()))
            .willThrow(new IndexOutOfBoundsException("failed encryption"));

      final PasswordPrivacyStrategy unitUnderTest = new PasswordPrivacyStrategy(encryptor, PASSWORD);

      assertThrows(IndexOutOfBoundsException.class,
            () -> unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA)));
   }

   @Test
   public void shouldReturnEncryptedWithPassword() throws IOException {
      final PasswordPrivacyStrategy unitUnderTest = PasswordPrivacyStrategy.create(PASSWORD);

      final InputStream encrypted = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA));

      assertFalse(Arrays.equals(SAMPLE_DATA, IOUtils.toByteArray(encrypted)));
   }

   @Test
   public void propagatesEncryptorDecException() {
      given(encryptor.decryptStream(Mockito.any(), Mockito.any()))
            .willThrow(new IndexOutOfBoundsException("failed encryption"));

      final PasswordPrivacyStrategy unitUnderTest = new PasswordPrivacyStrategy(encryptor, PASSWORD);

      assertThrows(IndexOutOfBoundsException.class,
            () -> unitUnderTest.decryptStream(new ByteArrayInputStream(SAMPLE_DATA)));
   }

   @Test
   public void shouldReturnDecryptedWithPassword() throws IOException {
      final PasswordPrivacyStrategy unitUnderTest = PasswordPrivacyStrategy.create(PASSWORD);
      final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA));

      final InputStream decrypted = unitUnderTest.decryptStream(encryptedStream);

      assertArrayEquals(SAMPLE_DATA, IOUtils.toByteArray(decrypted));
   }

}
