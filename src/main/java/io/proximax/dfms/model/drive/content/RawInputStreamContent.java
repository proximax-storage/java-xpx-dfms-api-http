/**
 * 
 */
package io.proximax.dfms.model.drive.content;

import java.io.InputStream;
import java.util.Optional;

import okhttp3.MediaType;

/**
 * raw content implementation simply forwarding content of input stream
 */
public class RawInputStreamContent extends InputStreamContent {

   private final MediaType contentType;
   
   /**
    * create new content
    * 
    * @param name arbitrary name of the content
    * @param inputStream input stream containing request body
    * @param contentType content type of data in the input stream
    */
   public RawInputStreamContent(Optional<String> name, InputStream inputStream, String contentType) {
      super(name, inputStream);
      this.contentType = MediaType.get(contentType);
   }

   /**
    * @return the contentType
    */
   public MediaType getContentType() {
      return contentType;
   }
}