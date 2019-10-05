package io.proximax.dfms.drive;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InputStreamContent extends BaseContent {
   private final InputStream inputStream;

   public InputStreamContent(Optional<String> name, InputStream inputStream) {
      super(name, false);
      this.inputStream = inputStream;
   }

   @Override
   public InputStream getInputStream() {
      return inputStream;
   }

   @Override
   public List<DriveContent> getChildren() {
      return Collections.emptyList();
   }

}
