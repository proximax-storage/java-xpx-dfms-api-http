package io.proximax.dfms.privacy.strategy;

import static io.proximax.core.utils.PasswordUtils.MINIMUM_PASSWORD_LENGTH;

import java.io.InputStream;

import org.apache.commons.lang3.Validate;

import io.proximax.cipher.PBECipherEncryptor;
import io.proximax.core.utils.PasswordUtils;
import io.proximax.dfms.model.PrivacyType;

/**
 * The privacy strategy that secures the data using a long password, 50 characters minimum.
 */
public final class PasswordPrivacyStrategy extends PrivacyStrategy {

   private final PBECipherEncryptor pbeCipherEncryptor;

   private final char[] passwordCharArray;
   private final String password;

   PasswordPrivacyStrategy(PBECipherEncryptor pbeCipherEncryptor, String password) {
      Validate.notNull(password, "password is required");
      Validate.isTrue(password.length() >= MINIMUM_PASSWORD_LENGTH,
            "minimum length for password is %d",
            MINIMUM_PASSWORD_LENGTH);

      this.pbeCipherEncryptor = pbeCipherEncryptor;
      this.password = password;
      this.passwordCharArray = password.toCharArray();
   }

   /**
    * Get the privacy type which is set as PASSWORD
    * 
    * @return the privacy type's int value
    * @see PrivacyType
    */
   @Override
   public int getPrivacyType() {
      return PrivacyType.PASSWORD.getValue();
   }

   /**
    * Get the password
    * 
    * @return the password
    */
   public String getPassword() {
      return password;
   }

   /**
    * Encrypt byte stream with password
    * 
    * @param stream the byte stream to encrypt
    * @return the encrypted byte stream
    */
   @Override
   public final InputStream encryptStream(final InputStream stream) {
      return pbeCipherEncryptor.encryptStream(stream, passwordCharArray);
   }

   /**
    * Decrypt byte stream with password
    * 
    * @param encryptedStream the byte stream to decrypt
    * @return the decrypted byte stream
    */
   @Override
   public final InputStream decryptStream(final InputStream encryptedStream) {
      return pbeCipherEncryptor.decryptStream(encryptedStream, passwordCharArray);

   }

   /**
    * Create instance of this strategy
    * 
    * @param password the password
    * @return the instance of this strategy
    */
   public static PasswordPrivacyStrategy create(String password) {
      return new PasswordPrivacyStrategy(new PBECipherEncryptor(), password);
   }

   /**
    * Create instance of this strategy and generate password
    * 
    * @return the instance of this strategy
    */
   public static PasswordPrivacyStrategy create() {
      return new PasswordPrivacyStrategy(new PBECipherEncryptor(), PasswordUtils.generatePassword());
   }
}
