package io.proximax.dfms.model.drive.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.proximax.dfms.model.drive.DriveContent;

class DirectoryContentTest {

   @Test
   void test() {
      DriveContent child = Mockito.mock(DriveContent.class);
      DirectoryContent content = new DirectoryContent(Optional.of("hello"), child);

      assertEquals(Optional.of("hello"), content.getName());
      assertEquals(1, content.getChildren().size());
      assertEquals(child, content.getChildren().get(0));
      assertThrows(IllegalStateException.class, content::getInputStream);
   }

}
