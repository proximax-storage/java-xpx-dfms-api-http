package io.proximax.dfms.privacy.strategies;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import io.proximax.core.crypto.CryptoEngines;
import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.dfms.cipher.BlockchainKeysCipherEncryptor;
import io.proximax.dfms.privacy.PrivacyType;
import io.proximax.dfms.privacy.strategies.ProximaXKeysPrivacyStrategy;

public class ProximaXPrivacyStrategyTest {

   private static final byte[] SAMPLE_RAW_DATA = "the quick brown fox jumps over the lazy dog".getBytes();
   private static final byte[] SAMPLE_ENCRYPTED_DATA = "DIASYUOIDHKJckxhzkhkahdhsao".getBytes();

   private static final KeyPair SENDER_KEYPAIR = new KeyPair(
         PrivateKey.fromHexString("08871D3EB4CF3D6695A61E8E1B60DC64DCC9EED40F33D4848BF9079168CCD4A4"));

   private static final KeyPair RECEIVER_KEYPAIR = new KeyPair(
         PrivateKey.fromHexString("1A5B81AE8830B8A79232CD366552AF6496FE548B4A23D4173FEEBA41B8ABA81F"));

   @Mock
   private BlockchainKeysCipherEncryptor encryptor;

   @BeforeEach
   public void setUp() {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void shouldReturnCorrectPrivacyType() {
      final int privacyType = new ProximaXKeysPrivacyStrategy(encryptor, SENDER_KEYPAIR.getPrivateKey().toString(),
            RECEIVER_KEYPAIR.getPublicKey().toString()).getPrivacyType();

      assertEquals(PrivacyType.PROXIMAX_KEYS.getValue(), privacyType);
   }

   @Test
   public void testStaticConstructor() {
      ProximaXKeysPrivacyStrategy strategy = ProximaXKeysPrivacyStrategy
            .create(SENDER_KEYPAIR.getPrivateKey().toString(), SENDER_KEYPAIR.getPublicKey().toString());

      assertEquals(PrivacyType.PROXIMAX_KEYS.getValue(), strategy.getPrivacyType());
   }

   @Test
   public void failInitWithoutPrivateKey() {
      assertThrows(NullPointerException.class,
            () -> new ProximaXKeysPrivacyStrategy(encryptor, null, RECEIVER_KEYPAIR.getPublicKey().toString()));
   }

   @Test
   public void failInitWithoutPublicKey() {
      assertThrows(NullPointerException.class,
            () -> new ProximaXKeysPrivacyStrategy(encryptor, SENDER_KEYPAIR.getPrivateKey().toString(), null));
   }

   @Test
   public void shouldReturnEncryptedWithKeys() {
      InputStream dummyEncryptedStream = new ByteArrayInputStream(SAMPLE_ENCRYPTED_DATA);
      given(encryptor.encryptStream(Mockito.any(), Mockito.any(), Mockito.any())).willReturn(dummyEncryptedStream);

      final ProximaXKeysPrivacyStrategy unitUnderTest = new ProximaXKeysPrivacyStrategy(encryptor,
            SENDER_KEYPAIR.getPrivateKey().toString(), RECEIVER_KEYPAIR.getPublicKey().toString());

      final InputStream encrypted = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_RAW_DATA));

      // just compare objects for equality
      assertEquals(dummyEncryptedStream, encrypted);
   }

   @Test
   public void shouldReturnDecryptedWithKeys() throws IOException {
      InputStream dummyDecryptedStream = new ByteArrayInputStream(SAMPLE_RAW_DATA);
      given(encryptor.decryptStream(Mockito.any(), Mockito.any(), Mockito.any())).willReturn(dummyDecryptedStream);

      final ProximaXKeysPrivacyStrategy unitUnderTest = new ProximaXKeysPrivacyStrategy(encryptor,
            SENDER_KEYPAIR.getPrivateKey().toString(), RECEIVER_KEYPAIR.getPublicKey().toString());

      final InputStream decrypted = unitUnderTest.decryptStream(new ByteArrayInputStream(SAMPLE_ENCRYPTED_DATA));

      // just compare objects for equality
      assertEquals(dummyDecryptedStream, decrypted);
   }

   @Test
   public void shouldReturnSameEncrypted() throws IOException {
      final ProximaXKeysPrivacyStrategy unitUnderTest = new ProximaXKeysPrivacyStrategy(
            new BlockchainKeysCipherEncryptor(), SENDER_KEYPAIR.getPrivateKey().toString(),
            RECEIVER_KEYPAIR.getPublicKey().toString());

      final InputStream encrypted = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_RAW_DATA));

      assertFalse(Arrays.equals(SAMPLE_RAW_DATA, IOUtils.toByteArray(encrypted)));
   }

   @Test
   public void shouldReturnSameDecrypted() throws IOException {
      final ProximaXKeysPrivacyStrategy unitUnderTest = new ProximaXKeysPrivacyStrategy(
            new BlockchainKeysCipherEncryptor(), SENDER_KEYPAIR.getPrivateKey().toString(),
            RECEIVER_KEYPAIR.getPublicKey().toString());

      final InputStream decrypted = unitUnderTest.decryptStream(new ByteArrayInputStream(sampleEncryptedData()));

      assertArrayEquals(SAMPLE_RAW_DATA, IOUtils.toByteArray(decrypted));
   }

   private byte[] sampleEncryptedData() {
      return CryptoEngines.defaultEngine().createBlockCipher(new KeyPair(SENDER_KEYPAIR.getPrivateKey()),
            new KeyPair(RECEIVER_KEYPAIR.getPublicKey())).encrypt(SAMPLE_RAW_DATA);
   }
}
