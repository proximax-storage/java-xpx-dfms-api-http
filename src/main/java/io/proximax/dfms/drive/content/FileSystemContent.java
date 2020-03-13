package io.proximax.dfms.drive.content;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.proximax.dfms.drive.DriveContent;
import io.proximax.dfms.exception.DFMSRuntimeException;
import io.proximax.dfms.utils.FileSystemUtils;

/**
 * represents contents defined by a path. This can be either actual content or node
 */
public class FileSystemContent extends BaseContent {
   private final Path source;

   public FileSystemContent(Path source) {
      // Files.isDirectory seems to have performance issues in Java8 so using File as intermediary
      super(Optional.of(FileSystemUtils.encode(source.getFileName())), source.toFile().isDirectory());
      this.source = source;
   }

   @Override
   public InputStream getInputStream() throws IOException {
      return Files.newInputStream(source);
   }

   @Override
   public List<DriveContent> getChildren() {
      if (isNode()) {
         try (Stream<Path> files = Files.list(source)) {
            return files.map(FileSystemContent::new).collect(Collectors.toList());
         } catch (IOException e) {
            throw new DFMSRuntimeException("Failed to traverse directory", e);
         }
      }
      // default return empty list
      return Collections.emptyList();
   }
}
