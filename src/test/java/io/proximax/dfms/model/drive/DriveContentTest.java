package io.proximax.dfms.model.drive;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;


class DriveContentTest {

   @Test
   void testFileFromPath() throws IOException {
      DriveContent dc = DriveContent.from(Paths.get("src/test/resources/files/test_pdf_file_2.pdf"));
      try (InputStream is = dc.getInputStream()) {
         assertEquals(1843249, IOUtils.toByteArray(is).length);
      }
      assertEquals(Optional.of("test_pdf_file_2.pdf"), dc.getName());
      assertFalse(dc.isNode());
      assertEquals(0, dc.getChildren().size());
   }

   @Test
   void testDirFromPath() throws IOException {
      DriveContent dc = DriveContent.from(Paths.get("src/test/resources/files"));
      assertEquals(Optional.of("files"), dc.getName());
      assertTrue(dc.isNode());
      assertEquals(8, dc.getChildren().size());
   }

   @Test
   void testFromByteArray() throws IOException {
      DriveContent dc = DriveContent.from(Optional.of("hellobyte"), new byte[] {1,5,2,9});
      try (InputStream is = dc.getInputStream()) {
         byte[] arr = IOUtils.toByteArray(is);
         assertEquals(4, arr.length);
         assertArrayEquals(new byte[] {1,5,2,9}, arr);
      }
      assertEquals(Optional.of("hellobyte"), dc.getName());
      assertFalse(dc.isNode());
      assertEquals(0, dc.getChildren().size());
   }

   @Test
   void testFromInputStream() throws IOException {
      InputStream inputStream = new ByteArrayInputStream(new byte[] {4,2,7,4,0});
      DriveContent dc = DriveContent.from(Optional.of("helloinput"), inputStream);
      try (InputStream is = dc.getInputStream()) {
         byte[] arr = IOUtils.toByteArray(is);
         assertEquals(5, arr.length);
         assertArrayEquals(new byte[] {4,2,7,4,0}, arr);
      }
      assertEquals(Optional.of("helloinput"), dc.getName());
      assertFalse(dc.isNode());
      assertEquals(0, dc.getChildren().size());
   }

   @Test
   void testFromDirectory() {
      DriveContent dcFile = DriveContent.from(Paths.get("src/test/resources/files/test_pdf_file_2.pdf"));
      DriveContent dcArr = DriveContent.from(Optional.of("hellobyte"), new byte[] {1,5,2,9});
      DriveContent dc = DriveContent.from(Optional.of("base elem"), Arrays.asList(dcFile, dcArr));
      
      assertEquals(Optional.of("base elem"), dc.getName());
      assertTrue(dc.isNode());
      assertEquals(Arrays.asList(dcFile, dcArr), dc.getChildren());
      assertThrows(IllegalStateException.class, () -> dc.getInputStream());
   }
}
