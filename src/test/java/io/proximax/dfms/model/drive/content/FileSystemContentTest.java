package io.proximax.dfms.model.drive.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class FileSystemContentTest {

   @Test
   void testFile() throws IOException {
      FileSystemContent content = new FileSystemContent(new File("src/test/resources/simple/text1.txt").toPath());

      assertEquals(Optional.of("text1.txt"), content.getName());
      try (InputStream is = content.getInputStream()) {
         assertEquals('t', is.read());
         assertEquals('e', is.read());
         assertEquals('x', is.read());
         assertEquals('t', is.read());
      }
      assertFalse(content.isNode());
      assertTrue(content.getChildren().isEmpty());
   }

   @Test
   void testDir() {
      FileSystemContent content = new FileSystemContent(new File("src/test/resources/simple").toPath());

      assertEquals(Optional.of("simple"), content.getName());
      assertThrows(IllegalStateException.class, content::getInputStream);
      assertFalse(content.getChildren().isEmpty());
      assertTrue(content.isNode());

   }

}
