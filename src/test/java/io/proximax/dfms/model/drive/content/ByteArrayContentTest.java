package io.proximax.dfms.model.drive.content;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class ByteArrayContentTest {

   @Test
   void testConstructor() throws IOException {
      ByteArrayContent content = new ByteArrayContent(Optional.of("hello"), new byte[] {1, 3, 7, 0});
      
      assertEquals(Optional.of("hello"), content.getName());
      try (InputStream is = content.getInputStream()) {
         assertEquals(1, is.read());
         assertEquals(3, is.read());
         assertEquals(7, is.read());
         assertEquals(0, is.read());
         assertEquals(-1, is.read());
      }
      assertTrue(content.getChildren().isEmpty());
   }

}
