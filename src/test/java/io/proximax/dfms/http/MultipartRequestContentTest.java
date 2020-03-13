/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import io.proximax.dfms.drive.DriveContent;
import io.proximax.dfms.drive.content.FileSystemContent;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * TODO add proper description
 */
class MultipartRequestContentTest {

   private static final String BOUNDARY = "ex6QCAsqaYnQ8dRHsLZCFroQsPkcHGuX";
   
   @Test
   void testDirectoryWithFiles() throws IOException {
      DriveContent content = new FileSystemContent(Paths.get(".", "src", "test", "resources", "simple"));
      RequestBody body = new MultipartRequestContent(content, BOUNDARY);
      Buffer sink = new Buffer();
      body.writeTo(sink);
      writeToDisk("simple-dir.txt", sink);
      assertBufferContent(sink, "simple-dir.txt");
   }

   @Test
   void testSingleFile() throws IOException {
      DriveContent content = new FileSystemContent(Paths.get(".", "src", "test", "resources", "simple", "text1.txt"));
      RequestBody body = new MultipartRequestContent(content, BOUNDARY);
      Buffer sink = new Buffer();
      body.writeTo(sink);
      writeToDisk("simple-file.txt", sink);
      assertBufferContent(sink, "simple-file.txt");      
   }
   
   /**
    * write content from buffer to a file identified by filename
    * 
    * @param fileName
    * @param buffer
    * @throws IOException
    */
   protected static void writeToDisk(String fileName, Buffer buffer) throws IOException {
      final File target = Paths.get(".", "src", "test", "resources", "multipart", fileName).toFile();
      try (FileOutputStream fos = new FileOutputStream(target)) {
         buffer.copyTo(fos);
      }
   }
   
   /**
    * assert that buffer and specified file have identical content
    * 
    * @param buffer
    * @param fileName
    * @throws FileNotFoundException
    * @throws IOException
    */
   protected static void assertBufferContent(Buffer buffer, String fileName) throws FileNotFoundException, IOException {
      final File expected = Paths.get(".", "src", "test", "resources", "multipart", fileName).toFile();
      try (FileInputStream fis = new FileInputStream(expected)) {
         assertTrue(IOUtils.contentEquals(buffer.inputStream(), fis));
      }
   }
}
