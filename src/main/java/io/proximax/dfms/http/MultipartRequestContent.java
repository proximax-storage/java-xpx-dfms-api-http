package io.proximax.dfms.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.Validate;

import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.utils.HttpUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * <p>
 * IPFS-like request body describing the files and directories that are being uploaded.
 * </p>
 * <p>
 * NOTE that the content is placed on the path not to the path. That beans the root element name is lost
 * </p>
 */
public class MultipartRequestContent extends RequestBody {
   /** random number generator for boundary generator */
   private static final Random RANDOM = new Random();
   /** root path for the content */
   private static final Path ROOT_PATH = Paths.get("");

   // character sequences
   private static final String LINE_FEED = "\r\n";
   private static final String QUOTE = "\"";
   private static final String SEMI_COLON = ";";
   private static final String DASH_DASH = "--";
   private static final String BOUNDARY_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
   private static final int BOUNDARY_SIZE = 32;

   /** the content to be serialized in request body */
   private final DriveContent content;

   /** random boundary used to separate the form parts */
   private final String boundary;
   /** content type of the request */
   private final MediaType contentType;

   /**
    * create new instance
    * 
    * @param content the drive content used to create request body
    * @param boundary the boundary that separates parts in the multi-part content
    */
   public MultipartRequestContent(DriveContent content, String boundary) {
      // validate input
      Validate.notNull(content, "Content can not be null");
      // store input
      this.content = content;
      // create boundary and content type
      this.boundary = boundary;
      this.contentType = MediaType.get("multipart/form-data; boundary=" + boundary);
   }

   /**
    * create new instance with random boundary
    * 
    * @param content the drive content used to create request body
    */
   public MultipartRequestContent(DriveContent content) {
      this(content, createBoundary());
   }

   @Override
   public MediaType contentType() {
      return contentType;
   }

   @Override
   public void writeTo(BufferedSink sink) throws IOException {
      if (content.isNode()) {
         addDirectoryContent(sink, ROOT_PATH, content);
      } else {
         addFile(sink, "file", ROOT_PATH, content);
      }
      // write last boundary at the end
      writeBoundary(sink, true);
   }

   /**
    * create random boundary string to separate multi-part form fields
    * 
    * @return boundary string
    */
   private static String createBoundary() {
      final int numChars = BOUNDARY_CHARS.length();
      final StringBuilder b = new StringBuilder();
      for (int i = 0; i < BOUNDARY_SIZE; i++) {
         b.append(BOUNDARY_CHARS.charAt(RANDOM.nextInt(numChars)));
      }
      return b.toString();
   }

   /**
    * write the boundary to the sink
    * 
    * @param sink the sink to write to
    * @param finalize if true then double-dash will be appended after boundary string
    * @throws IOException when write to sink failed
    */
   private void writeBoundary(BufferedSink sink, boolean finalize) throws IOException {
      sink.writeUtf8(DASH_DASH).writeUtf8(boundary);
      if (finalize) {
         sink.writeUtf8(DASH_DASH);
      }
      sink.writeUtf8(LINE_FEED);
   }

   /**
    * write directory content to the sink
    * 
    * @param sink the sink to write to
    * @param parentPath parent path of the content (or empty path for root)
    * @param content the content
    * @throws IOException when write to sink fails
    */
   private void addDirectoryContent(BufferedSink sink, Path parentPath, DriveContent content) throws IOException {
      Path dirPath = parentPath
            .resolve(content.getName().orElseThrow(() -> new IllegalArgumentException("Directory name is mandatory")));
      addDirectoryPart(sink, dirPath);
      for (DriveContent f : content.getChildren()) {
         if (f.isNode()) {
            // recursively add sub-directory
            addDirectoryContent(sink, dirPath, f);
         } else {
            // add file
            addFile(sink, "file", dirPath, f);
         }
      }
   }

   /**
    * write directory information to the sink
    * 
    * @param sink the sink to write to
    * @param path path of the content (or empty path for root)
    * @throws IOException when write to sink fails
    */
   private void addDirectoryPart(BufferedSink sink, Path path) throws IOException {
      writeBoundary(sink, false);
      sink.writeUtf8("Content-Disposition: file; filename=").writeUtf8(quote(path.toString())).writeUtf8(LINE_FEED);
      sink.writeUtf8("Content-Type: application/x-directory").writeUtf8(LINE_FEED);
      sink.writeUtf8("Content-Transfer-Encoding: binary").writeUtf8(LINE_FEED);
      sink.writeUtf8(LINE_FEED);
      sink.writeUtf8(LINE_FEED);
   }

   /**
    * write actual file data to the sink
    * 
    * @param sink the sink to write to
    * @param fieldName name of the file
    * @param parent path to the content
    * @param uploadFile the content
    * @throws IOException when write to sink fails
    */
   private void addFile(BufferedSink sink, String fieldName, Path parent, DriveContent uploadFile) throws IOException {
      writeBoundary(sink, false);
      Optional<String> fileName = uploadFile.getName().map(n -> parent.resolve(n).toString());
      if (fileName.isPresent()) {
         sink.writeUtf8("Content-Disposition: file; filename=").writeUtf8(quote(fileName.get())).writeUtf8(SEMI_COLON)
               .writeUtf8(LINE_FEED);
      } else {
         sink.writeUtf8("Content-Disposition: file; name=").writeUtf8(quote(fieldName)).writeUtf8(SEMI_COLON)
               .writeUtf8(LINE_FEED);
      }
      sink.writeUtf8("Content-Type: application/octet-stream").writeUtf8(LINE_FEED);
      sink.writeUtf8("Content-Transfer-Encoding: binary").writeUtf8(LINE_FEED);
      sink.writeUtf8(LINE_FEED);

      try (InputStream inputStream = uploadFile.getInputStream()) {
         byte[] buffer = new byte[4096];
         int r;
         while ((r = inputStream.read(buffer)) != -1) {
            sink.write(buffer, 0, r);
         }
      }

      sink.writeUtf8(LINE_FEED);
   }

   /**
    * return name encoded to be URL safe and between double quotes (e.g. "test%20name" is created from test name)
    * 
    * @param name the string to process
    * @return encoded and quoted string
    */
   private static String quote(String name) {
      return QUOTE + HttpUtils.encode(name) + QUOTE;
   }
}
