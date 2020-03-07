package io.proximax.dfms.privacy.strategy;

import java.io.InputStream;

import org.apache.commons.lang3.Validate;

import io.proximax.cipher.BlockchainKeysCipherEncryptor;
import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.model.PrivacyType;

/**
 * The privacy strategy that secures data using the NEM keys (a private key and a public key). This strategy encrypt and
 * decrypt the data using both private and public keys
 */
public final class NemKeysPrivacyStrategy extends PrivacyStrategy {

   private final BlockchainKeysCipherEncryptor blockchainKeysCipherEncryptor;
   private final KeyPair keyPairOfPrivateKey;
   private final KeyPair keyPairOfPublicKey;

   NemKeysPrivacyStrategy(BlockchainKeysCipherEncryptor blockchainKeysCipherEncryptor, String privateKey,
         String publicKey) {

      Validate.notNull(privateKey, "private key is required");
      Validate.notNull(publicKey, "public key is required");

      this.blockchainKeysCipherEncryptor = blockchainKeysCipherEncryptor;
      this.keyPairOfPrivateKey = new KeyPair(PrivateKey.fromHexString(privateKey));
      this.keyPairOfPublicKey = new KeyPair(PublicKey.fromHexString(publicKey));
   }

   /**
    * Get the privacy type which is set as NEMKEYS
    * 
    * @return the privacy type's int value
    * @see PrivacyType
    */
   @Override
   public int getPrivacyType() {
      return PrivacyType.NEMKEYS.getValue();
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
   public static NemKeysPrivacyStrategy create(String privateKey, String publicKey) {
      return new NemKeysPrivacyStrategy(new BlockchainKeysCipherEncryptor(), privateKey, publicKey);
   }
}
