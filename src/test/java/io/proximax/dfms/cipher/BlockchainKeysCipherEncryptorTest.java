package io.proximax.dfms.cipher;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.cipher.BlockchainKeysCipherEncryptor;

public class BlockchainKeysCipherEncryptorTest {

    public static final byte[] SAMPLE_DATA = getSampleBytes();

    public static final String PRIVATE_KEY_1 = "8374B5915AEAB6308C34368B15ABF33C79FD7FEFC0DEAF9CC51BA57F120F1190";
    public static final String PUBLIC_KEY_1 = "9E7930144DA0845361F650BF78A36791ABF2577E251706ECA45480998FE61D18";

    public static final String PRIVATE_KEY_2 = "369CB3195F88A16F8326DABBD37DA5F8458B55AA5DA6F7E2F756A12BE6CAA546";
    public static final String PUBLIC_KEY_2 = "8E1A94D534EA6A3B02B0B967701549C21724C7644B2E4C20BF15D01D50097ACB";

    public static final String PRIVATE_KEY_3 = "49DCE5457D6D83983FB4C28F0E58668DA656F8BB46AAFEEC800EBD420E1FDED5";
    public static final String PUBLIC_KEY_3 = "353BCDAF409724F9E3F7D1246E91BFD3EC1782D9C04D72C0C162F4B346126E45";

    private BlockchainKeysCipherEncryptor unitUnderTest;

    @BeforeEach
    public void setUp() {
        unitUnderTest = new BlockchainKeysCipherEncryptor();
    }

    @Test
    public void shouldNotReturnOriginalDataOnEncrypt() throws IOException {
        final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA),
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_1)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_2)));

        assertFalse(Arrays.equals(IOUtils.toByteArray(encryptedStream), SAMPLE_DATA));
    }

    @Test
    public void shouldReturnDecryptedDataOnDecryptForSameKeys() throws IOException {
        final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA),
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_1)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_2)));

        final InputStream decrypted = unitUnderTest.decryptStream(encryptedStream,
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_1)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_2)));

        assertArrayEquals(IOUtils.toByteArray(decrypted), SAMPLE_DATA);
    }

    @Test
    public void shouldReturnDecryptedDataOnDecryptForOppositeKeys() throws IOException {
        final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA),
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_1)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_2)));

        final InputStream decrypted = unitUnderTest.decryptStream(encryptedStream,
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_2)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_1)));

        assertArrayEquals(IOUtils.toByteArray(decrypted), SAMPLE_DATA);
    }

    @Test
    public void failOnDecryptWhenIncorrectPrivateKey() {
        final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA),
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_1)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_2)));

        final InputStream decrypted = unitUnderTest.decryptStream(encryptedStream,
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_3)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_1)));

        assertThrows(IOException.class, () -> IOUtils.toByteArray(decrypted));
    }

    @Test
    public void failOnDecryptWhenIncorrectPublicKey() {
        final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA),
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_1)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_2)));

        final InputStream decrypted = unitUnderTest.decryptStream(encryptedStream,
                new KeyPair(PrivateKey.fromHexString(PRIVATE_KEY_1)),
                new KeyPair(PublicKey.fromHexString(PUBLIC_KEY_3)));

        assertThrows(IOException.class, () -> IOUtils.toByteArray(decrypted));
    }

    private static byte[] getSampleBytes() {
        try {
            return FileUtils.readFileToByteArray(new File("src/test/resources/files/test_pdf_file_1.pdf"));
        } catch (IOException e) {
           fail("could not locate resource file");
           return null;
        }
    }
}
