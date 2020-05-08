/**
 * 
 */
package io.proximax.dfms.http.repos;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.http.MultipartRequestContent;
import io.proximax.dfms.http.RawMultipartRequestContent;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.content.RawInputStreamContent;
import okhttp3.RequestBody;

/**
 * {@link DriveHttp} tests
 */
class DriveHttpTest {

   @Test
   void testStandardContentFactory() {
      DriveContent content = DriveContent.from(Optional.of("hello"), new byte[] { 1, 2, 3 });
      RequestBody body = DriveHttp.createRequestBody(content);
      assertTrue(body instanceof MultipartRequestContent);
   }

   @Test
   void testRawContentFactory() {
      DriveContent content = new RawInputStreamContent(Optional.of("hello"),
            new ByteArrayInputStream(new byte[] { 1, 2, 3 }),
            "multipart/form-data; boundary=" + MultipartRequestContent.createBoundary());
      RequestBody body = DriveHttp.createRequestBody(content);
      assertTrue(body instanceof RawMultipartRequestContent);
   }

}
