/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * {@link PasswordUtils} tests
 */
class PasswordUtilsTest {
   private static final int MIN_LEN = PasswordUtils.MINIMUM_PASSWORD_LENGTH;

   @Test
   public void shouldGenerateCharPasswordWithDefaultLength() {
       final String generatedPassword = PasswordUtils.generatePassword();

       assertNotNull(generatedPassword);
       assertTrue(generatedPassword.length() == MIN_LEN);
   }

   @Test
   public void shouldGeneratePasswordWithSpecialCharacters() {
       final String generatedPassword = PasswordUtils.generatePassword(true);

       assertNotNull(generatedPassword);
       assertTrue(generatedPassword.length() == MIN_LEN);
   }

   @Test
   public void shouldGeneratePasswordWithCustomLength() {
      assertTrue(PasswordUtils.generatePassword(MIN_LEN).length() == MIN_LEN);
      assertTrue(PasswordUtils.generatePassword(MIN_LEN+5).length() == MIN_LEN+5);
      assertTrue(PasswordUtils.generatePassword(MIN_LEN*2).length() == MIN_LEN*2);
      assertTrue(PasswordUtils.generatePassword(MIN_LEN*10).length() == MIN_LEN*10);
   }

   @Test
   public void failWhenGeneratingPasswordBelowMinimumLength() {
      assertThrows(IllegalArgumentException.class, () -> PasswordUtils.generatePassword(MIN_LEN - 1));
   }

}
