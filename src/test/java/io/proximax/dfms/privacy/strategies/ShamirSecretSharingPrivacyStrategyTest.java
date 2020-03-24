package io.proximax.dfms.privacy.strategies;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import com.codahale.shamir.Scheme;

import io.proximax.dfms.privacy.PrivacyType;
import io.proximax.dfms.privacy.strategies.ShamirSecretSharingPrivacyStrategy.SecretPart;

public class ShamirSecretSharingPrivacyStrategyTest {

   private static final byte[] SAMPLE_DATA = "the quick brown fox jumps over the lazy dog".getBytes();

   private static final byte[] SECRET = ("dhsakdhsauihcxznmneywquidkjsanjcbnxztyduisyaheqkwjncbxzcgyuiagsdiujasodjk"
         + "wqehqtsyiudhsandmbsamnbcxzygcahgisudhasnmbdmnasbcysagciugxziuhkjsbdamndnmsabfgaduishadshakdnsakdbsajbeh"
         + "wqyuieyqwiueyqwohdsanlnalsfjkhgkzgmnbcmnxzbhjgdsajgduisayiuyewquehwqkjbeqwnbdmnabsdabjshgdasudhgsuakghk"
         + "cbxzcbajsbdkasjgkjhwgquegqwbzmcbmzxn").getBytes();

   private static final int SECRET_TOTAL_PART_COUNT = 5;

   private static final int SECRET_MINIMUM_PART_COUNT_TO_BUILD = 3;

   private static final Scheme SCHEME = Scheme.of(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD);

   private static final Map<Integer, byte[]> SECRET_PARTS = SCHEME.split(SECRET);

   @Test
   void testStaticCreate() throws IOException {
      final int totalParts = 3;
      final int minParts = 2;
      final Scheme scheme = Scheme.of(totalParts, minParts);
      final Map<Integer, byte[]> parts = scheme.split(SECRET);
      final SecretPart part1 = new SecretPart(1, parts.get(1));
      final SecretPart part2 = new SecretPart(2, parts.get(2));
      final SecretPart part3 = new SecretPart(3, parts.get(3));
      final ShamirSecretSharingPrivacyStrategy encryptor = ShamirSecretSharingPrivacyStrategy
            .create(totalParts, minParts, part1, part3);
      final InputStream encrypted = encryptor.encryptStream(new ByteArrayInputStream(SAMPLE_DATA));
      // make sure output is not he same as input
      final ShamirSecretSharingPrivacyStrategy decryptor = ShamirSecretSharingPrivacyStrategy
            .create(totalParts, minParts, part2, part3);
      InputStream decrypted = decryptor.decryptStream(encrypted);
      assertTrue(Arrays.equals(SAMPLE_DATA, IOUtils.toByteArray(decrypted)));
   }
   
   @Test
   public void shouldReturnCorrectPrivacyType() {
      final int privacyType = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, SECRET_PARTS).getPrivacyType();

      assertEquals(PrivacyType.SHAMIR.getValue(), privacyType);
   }

   @Test
   public void failWithNegativeSecretTotalPartCount() {
      assertThrows(IllegalArgumentException.class,
            () -> ShamirSecretSharingPrivacyStrategy.create(-1, SECRET_MINIMUM_PART_COUNT_TO_BUILD, SECRET_PARTS));
   }

   @Test
   public void failWithNegativeSecretMinimumPartCountToBuild() {
      assertThrows(IllegalArgumentException.class,
            () -> ShamirSecretSharingPrivacyStrategy.create(SECRET_TOTAL_PART_COUNT, -1, SECRET_PARTS));
   }

   @Test
   public void failWithSecretMinimumPartCountToBuildBeingGreaterThanTotalPartCount() {
      assertThrows(IllegalArgumentException.class,
            () -> ShamirSecretSharingPrivacyStrategy
                  .create(SECRET_TOTAL_PART_COUNT, SECRET_TOTAL_PART_COUNT + 1, SECRET_PARTS));
   }

   @Test
   public void failWithSecretPartsEmpty() {
      assertThrows(IllegalArgumentException.class,
            () -> ShamirSecretSharingPrivacyStrategy
                  .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, Collections.emptyMap()));
   }

   @Test
   public void failWithSecretPartsNotMeetingMinimumPartCountToBuild() {
      assertThrows(IllegalArgumentException.class,
            () -> ShamirSecretSharingPrivacyStrategy.create(SECRET_TOTAL_PART_COUNT,
                  SECRET_MINIMUM_PART_COUNT_TO_BUILD,
                  Collections.singletonMap(1, SECRET_PARTS.get(1))));
   }

   @Test
   public void returnEncryptedWithAllSecretParts() throws IOException {
      final ShamirSecretSharingPrivacyStrategy unitUnderTest = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, SECRET_PARTS);

      final InputStream encrypted = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA));

      assertFalse(Arrays.equals(SAMPLE_DATA, IOUtils.toByteArray(encrypted)));
   }

   @Test
   public void returnEncryptedWithMinimumSecretParts() throws IOException {
      final Map<Integer, byte[]> minimumSecretParts = new HashMap<>();
      minimumSecretParts.put(1, SECRET_PARTS.get(1));
      minimumSecretParts.put(3, SECRET_PARTS.get(3));
      minimumSecretParts.put(4, SECRET_PARTS.get(4));
      final ShamirSecretSharingPrivacyStrategy unitUnderTest = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, minimumSecretParts);

      final InputStream encrypted = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA));

      assertFalse(Arrays.equals(SAMPLE_DATA, IOUtils.toByteArray(encrypted)));
   }

   @Test
   public void returnDecryptedWithMinimumSecretParts() throws IOException {
      final ShamirSecretSharingPrivacyStrategy allPartsStrategy = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, SECRET_PARTS);
      final InputStream encryptedStream = allPartsStrategy.encryptStream(new ByteArrayInputStream(SAMPLE_DATA));
      final Map<Integer, byte[]> minimumSecretParts = new HashMap<>();
      minimumSecretParts.put(1, SECRET_PARTS.get(1));
      minimumSecretParts.put(2, SECRET_PARTS.get(2));
      minimumSecretParts.put(4, SECRET_PARTS.get(4));
      final ShamirSecretSharingPrivacyStrategy unitUnderTest = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, minimumSecretParts);

      final InputStream decrypted = unitUnderTest.decryptStream(encryptedStream);

      assertArrayEquals(SAMPLE_DATA, IOUtils.toByteArray(decrypted));
   }

   @Test
   public void returnDecryptedWithDifferentSecretParts() throws IOException {
      final Map<Integer, byte[]> firstSecretParts = new HashMap<>();
      firstSecretParts.put(2, SECRET_PARTS.get(2));
      firstSecretParts.put(3, SECRET_PARTS.get(3));
      firstSecretParts.put(5, SECRET_PARTS.get(5));
      final ShamirSecretSharingPrivacyStrategy firstPartsStrategy = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, firstSecretParts);
      final InputStream encryptedStream = firstPartsStrategy.encryptStream(new ByteArrayInputStream(SAMPLE_DATA));
      final Map<Integer, byte[]> secondSecretParts = new HashMap<>();
      secondSecretParts.put(1, SECRET_PARTS.get(1));
      secondSecretParts.put(2, SECRET_PARTS.get(2));
      secondSecretParts.put(4, SECRET_PARTS.get(4));
      final ShamirSecretSharingPrivacyStrategy secondPartsStrategy = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, secondSecretParts);

      final InputStream decrypted = secondPartsStrategy.decryptStream(encryptedStream);

      assertArrayEquals(SAMPLE_DATA, IOUtils.toByteArray(decrypted));
   }

   @Test
   public void returnDecryptedWithWrongSecretParts() throws IOException {
      final Map<Integer, byte[]> firstSecretParts = new HashMap<>();
      firstSecretParts.put(2, SECRET_PARTS.get(2));
      firstSecretParts.put(3, SECRET_PARTS.get(3));
      firstSecretParts.put(5, SECRET_PARTS.get(5));
      final ShamirSecretSharingPrivacyStrategy firstPartsStrategy = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, firstSecretParts);
      final InputStream encryptedStream = firstPartsStrategy.encryptStream(new ByteArrayInputStream(SAMPLE_DATA));
      final Map<Integer, byte[]> wrongSecretParts = new HashMap<>();
      wrongSecretParts.put(1, SECRET_PARTS.get(1));
      wrongSecretParts.put(3, SECRET_PARTS.get(3));
      wrongSecretParts.put(4, SECRET_PARTS.get(5));

      final ShamirSecretSharingPrivacyStrategy unitUnderTest = ShamirSecretSharingPrivacyStrategy
            .create(SECRET_TOTAL_PART_COUNT, SECRET_MINIMUM_PART_COUNT_TO_BUILD, wrongSecretParts);

      assertThrows(IOException.class, () -> IOUtils.toByteArray(unitUnderTest.decryptStream(encryptedStream)));
   }

   @Test
   void testSecretPart() {
      SecretPart part = new SecretPart(5, new byte[] {5,2,8,3});
      assertEquals(5, part.getIndex());
      assertArrayEquals(new byte[] {5,2,8,3}, part.getData());
   }
}
