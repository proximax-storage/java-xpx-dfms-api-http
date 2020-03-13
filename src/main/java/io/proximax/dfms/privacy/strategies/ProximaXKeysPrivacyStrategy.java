package io.proximax.dfms.privacy.strategies;

import java.io.InputStream;

import org.apache.commons.lang3.Validate;

import io.proximax.cipher.BlockchainKeysCipherEncryptor;
import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.privacy.PrivacyStrategy;
import io.proximax.dfms.privacy.PrivacyType;

/**
 * The privacy strategy that secures data using the ProximaX keys (a private key and a public key). This strategy
 * encrypts and decrypts the data using both private and public keys
 */
public final class ProximaXKeysPrivacyStrategy implements PrivacyStrategy {

   private final BlockchainKeysCipherEncryptor blockchainKeysCipherEncryptor;
   private final KeyPair keyPairOfPrivateKey;
   private final KeyPair keyPairOfPublicKey;

   ProximaXKeysPrivacyStrategy(BlockchainKeysCipherEncryptor blockchainKeysCipherEncryptor, String privateKey,
         String publicKey) {

      Validate.notNull(privateKey, "private key is required");
      Validate.notNull(publicKey, "public key is required");

      this.blockchainKeysCipherEncryptor = blockchainKeysCipherEncryptor;
      this.keyPairOfPrivateKey = new KeyPair(PrivateKey.fromHexString(privateKey));
      this.keyPairOfPublicKey = new KeyPair(PublicKey.fromHexString(publicKey));
   }

   /**
    * Get the privacy type which is set as PROXIMAX_KEYS
    * 
    * @return the privacy type's int value
    * @see PrivacyType
    */
   @Override
   public int getPrivacyType() {
      return PrivacyType.PROXIMAX_KEYS.getValue();
   }

   /**
    * Encrypt byte stream using the private and public keys provided
    * 
    * @param stream the byte stream to encrypt
    * @return the encrypted byte stream
    */
   @Override
   public final InputStream encryptStream(final InputStream stream) {
      return blockchainKeysCipherEncryptor.encryptStream(stream, keyPairOfPrivateKey, keyPairOfPublicKey);
   }

   /**
    * Encrypt byte stream using the private and public keys provided
    * 
    * @param encryptedStream the byte stream to decrypt
    * @return the decrypted byte stream
    */
   @Override
   public final InputStream decryptStream(final InputStream encryptedStream) {
      return blockchainKeysCipherEncryptor.decryptStream(encryptedStream, keyPairOfPrivateKey, keyPairOfPublicKey);
   }

   /**
    * Create instance of this strategy
    * 
    * @param privateKey the private key
    * @param publicKey the public key
    * @return the instance of this strategy
    */
   public static ProximaXKeysPrivacyStrategy create(String privateKey, String publicKey) {
      return new ProximaXKeysPrivacyStrategy(new BlockchainKeysCipherEncryptor(), privateKey, publicKey);
   }
}
