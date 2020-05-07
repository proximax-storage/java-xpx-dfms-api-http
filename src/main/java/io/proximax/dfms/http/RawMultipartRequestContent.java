/**
 * 
 */
package io.proximax.dfms.http;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * request body implementation used to simply forward existing form multipart request body serialized to input stream
 */
public class RawMultipartRequestContent extends RequestBody {

   private final MediaType contentType;
   private final InputStream is;
   
   /**
    * create new request body
    * 
    * @param contentType content type including the boundary
    * @param is input stream with the data
    */
   public RawMultipartRequestContent(MediaType contentType, InputStream is) {
      this.contentType = contentType;
      this.is = is;
   }

   @Override
   public MediaType contentType() {
      return contentType;
   }

   @Override
   public void writeTo(BufferedSink sink) throws IOException {
      byte[] buffer = new byte[4096];
      int r;
      while ((r = is.read(buffer)) != -1) {
         sink.write(buffer, 0, r);
      }
   }

   @Override
   public boolean isOneShot() {
      // return true to indicate that it is not possible to replay the serialization
      return true;
   }
}
