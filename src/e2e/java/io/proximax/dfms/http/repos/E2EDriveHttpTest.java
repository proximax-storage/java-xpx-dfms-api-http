/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import io.proximax.cid.Cid;
import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.drive.DriveContent;
import io.proximax.dfms.drive.FileSystemContent;

/**
 * TODO add proper description
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class E2EDriveHttpTest {

   private static final Cid CONTRACT = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");

   private final String path = "hello" + System.currentTimeMillis();// Long.toString(new Random().nextLong());

   private StorageApi api;
   private DriveRepository drive;

   @BeforeAll
   void init() throws MalformedURLException {
      api = new StorageApi(new URL("http://localhost:6366"));
      drive = api.createDriveRepository();
   }

   @Test
   void killServer() throws IOException {
      DriveContent addContent = new FileSystemContent(new File("src/e2e/resources/simple").toPath());
      drive.add(CONTRACT, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      drive.remove(CONTRACT, path + "/text1.txt").timeout(30, TimeUnit.SECONDS).blockingAwait();
      drive.remove(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingAwait();
   }
   
   @Test
   void test01AddDirectory() throws IOException {
      DriveContent addContent = new FileSystemContent(new File("src/e2e/resources/simple").toPath());
      Cid cid = drive.add(CONTRACT, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      System.out.println("ID of uploaded data: " + cid);
      // now make request for that data
      DriveContent content = drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
      try (InputStream is = content.getInputStream()) {
//         File tempFile = File.createTempFile("dfms-test-"+path, ".tar");
//         FileUtils.copyInputStreamToFile(is, tempFile);
////         writeToFile("/home/fiddis/tono/proximax/java-xpx-dfms-http-api/out-" + path + ".tar", is);
//         FileSystemManager fsManager = VFS.getManager();
//         FileObject tarredContent = fsManager.resolveFile( "tar:file://" + tempFile.getCanonicalPath()+"!/" );
//         System.out.println(tarredContent);
      }

   }

   @Test
   void test02CopyFile() throws IOException {
      String targetPath = path + "copy_dst";
      DriveContent content = drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
      try (InputStream is = content.getInputStream()) {
//         writeToFile("/home/fiddis/tono/proximax/java-xpx-dfms-http-api/out-" + path + ".tar", is);
      }
      drive.copy(CONTRACT, path, targetPath).timeout(30, TimeUnit.SECONDS).blockingAwait();
      DriveContent newContent = drive.get(CONTRACT, targetPath).timeout(30, TimeUnit.SECONDS).blockingFirst();
      try (InputStream is = newContent.getInputStream()) {
//         writeToFile("/home/fiddis/tono/proximax/java-xpx-dfms-http-api/out-" + targetPath + ".tar", is);
      }
   }

   @Test
   void test03MoveFile() throws IOException {
      String targetPath = path + "move_dst";
      DriveContent content = drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
      try (InputStream is = content.getInputStream()) {
//         writeToFile("/home/fiddis/tono/proximax/java-xpx-dfms-http-api/out-" + path + ".tar", is);
      }
      drive.move(CONTRACT, path, targetPath).timeout(30, TimeUnit.SECONDS).blockingAwait();
      DriveContent newContent = drive.get(CONTRACT, targetPath).timeout(30, TimeUnit.SECONDS).blockingFirst();
      try (InputStream is = newContent.getInputStream()) {
//         writeToFile("/home/fiddis/tono/proximax/java-xpx-dfms-http-api/out-" + targetPath + ".tar", is);
      }
      // move the stuff back
      drive.move(CONTRACT, targetPath, path).timeout(30, TimeUnit.SECONDS).blockingAwait();
   }

   @Test
   void test04Mkdir() throws IOException {
      // remove the content
      drive.makeDir(CONTRACT, path + "somedir").timeout(30, TimeUnit.SECONDS).blockingAwait();
      // now try to retrieve the content again
      drive.get(CONTRACT, path + "somedir").timeout(30, TimeUnit.SECONDS).blockingFirst();
   }

   @Test
//   @Disabled("Running this and then removal of parent dir breaks contract until restart........")
   void test04RemoveFile() throws IOException {
      // remove the content
      drive.remove(CONTRACT, path + "/text1.txt").timeout(30, TimeUnit.SECONDS).blockingAwait();
      // now try to retrieve the content again
      drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
   }

   @Test
   void test05RemoveAll() throws IOException, InterruptedException {
      // remove the content
      drive.remove(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingAwait();
      // now try to retrieve the content again
      assertThrows(RuntimeException.class,
            () -> drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst());
   }

   private static void writeToFile(String fileName, InputStream is) throws IOException {
      try (FileOutputStream fos = new FileOutputStream(fileName)) {
         int size;
         byte[] buff = new byte[1024];
         while ((size = is.read(buff)) >= 0) {
            fos.write(buff, 0, size);
         }
      }
   }
}
