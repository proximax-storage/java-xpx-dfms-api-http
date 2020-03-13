package io.proximax.dfms.drive.content;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.proximax.dfms.drive.DriveContent;

public class ByteArrayContent extends BaseContent {
   private final byte[] data;

   public ByteArrayContent(Optional<String> name, byte[] data) {
      super(name, false);
      this.data = data;
   }

   @Override
   public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(data);
   }

   @Override
   public List<DriveContent> getChildren() {
      return Collections.emptyList();
   }

}
