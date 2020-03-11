package io.proximax.cipher;

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

public class PBECipherEncryptorTest {

    public static final byte[] SAMPLE_DATA = getSampleBytes();

    public static final char[] PASSWORD = "lkNzBmYmYyNTExZjZmNDYyZTdjYWJmNmY1MjJiYjFmZTk3Zjg2NDA5ZDlhOD".toCharArray();

    private PBECipherEncryptor unitUnderTest;

    @BeforeEach
    public void setUp() {
        unitUnderTest = new PBECipherEncryptor();
    }

    @Test
    public void shouldReturnEncryptedDataOnEncrypt() throws IOException {
        final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA), PASSWORD);

        assertFalse(Arrays.equals(IOUtils.toByteArray(encryptedStream), SAMPLE_DATA));
    }

    @Test
    public void shouldReturnDecryptedDataOnDecrypt() throws IOException {
        final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA), PASSWORD);

        final InputStream decrypted = unitUnderTest.decryptStream(encryptedStream, PASSWORD);

        assertArrayEquals(IOUtils.toByteArray(decrypted), SAMPLE_DATA);
    }

    @Test
    public void failOnDecryptWhenIncorrectPassword() {
        final InputStream encryptedStream = unitUnderTest.encryptStream(new ByteArrayInputStream(SAMPLE_DATA), PASSWORD);

        final InputStream decrypted = unitUnderTest.decryptStream(encryptedStream, "password".toCharArray());

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
