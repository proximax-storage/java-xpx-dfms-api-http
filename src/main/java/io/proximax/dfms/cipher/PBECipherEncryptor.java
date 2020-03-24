package io.proximax.dfms.cipher;

import static java.util.Arrays.asList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Collections;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;

/**
 * This factory class to creating cipher for password based encryptions
 */
public class PBECipherEncryptor {

	private static final String CONST_ALGO_PBKDF2 = "PBKDF2WithHmacSHA256";

	/**
	 * Encrypt byte stream with password
	 *
	 * @param byteStream the byte stream to encrypt
	 * @param password the password
	 * @return the encrypted stream
	 */
	public InputStream encryptStream(InputStream byteStream, char[] password) {
		try {
			byte[] salt = new byte[32];
			byte[] iv = new byte[16];
			SecureRandom rand = new SecureRandom();
			rand.nextBytes(salt);
			rand.nextBytes(iv);

			Cipher cipher = getCipherInstance();
			cipher.init(Cipher.ENCRYPT_MODE,
					getPBESecretKey(password, salt),
					getIvParameterSpec(iv));

			return new SequenceInputStream(Collections.enumeration(asList(
					new ByteArrayInputStream(salt),
					new ByteArrayInputStream(iv),
					new CipherInputStream(byteStream, cipher)
			)));
		} catch (Exception e) {
			throw new RuntimeException("Failed to encrypt stream", e);
		}
	}

	/**
	 * Decrypt byte stream with password
	 *
	 * @param byteStream the encrypted byte stream
	 * @param password the password
	 * @return the decrypted stream
	 */
	public InputStream decryptStream(InputStream byteStream, char[] password) {
		try {
			byte[] salt = new byte[32];
			byte[] iv = new byte[16];
			IOUtils.read(byteStream, salt);
			IOUtils.read(byteStream, iv);

			Cipher cipher = getCipherInstance();
			cipher.init(Cipher.DECRYPT_MODE,
					getPBESecretKey(password, salt),
					getIvParameterSpec(iv));

			return new CipherInputStream(byteStream, cipher);
		} catch (Exception e) {
			throw new RuntimeException("Failed to decrypt stream", e);
		}
	}

	private SecretKey getPBESecretKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(CONST_ALGO_PBKDF2);
		KeySpec keyspec = new PBEKeySpec(password, salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(keyspec);
		return new SecretKeySpec(tmp.getEncoded(), "AES");
	}

	private Cipher getCipherInstance() throws NoSuchAlgorithmException, NoSuchPaddingException {
		return Cipher.getInstance("AES/CBC/PKCS5PADDING");
	}

	private IvParameterSpec getIvParameterSpec(byte[] iv) {
		return new IvParameterSpec(iv);
	}

}
