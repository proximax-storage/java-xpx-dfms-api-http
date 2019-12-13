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

import io.proximax.Cid;
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

   private static final Cid FIXED_CID = Cid
         .decode("08041220720a6dc299b7672e623cef70f9f6f827d81ea4b1d1f545854422a77502f10090");

   private StorageApi api;
   private DriveRepository drive;
   private String path = "hello" + System.currentTimeMillis();// Long.toString(new Random().nextLong());

   @BeforeAll
   void init() throws MalformedURLException {
      api = new StorageApi(new URL("http://localhost:6366"));
      drive = api.createDriveRepository();
   }

   @Test
   void test01AddDirecotry() throws IOException {
      DriveContent addContent = new FileSystemContent(
            new File("src/e2e/resources/simple").toPath());
      Cid cid = drive.add(FIXED_CID, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      System.out.println("ID of uploaded data: " + cid);
      // now make request for that data
      DriveContent content = drive.get(FIXED_CID, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
      try (InputStream is = content.getInputStream()) {
//         writeToFile("/home/fiddis/tono/proximax/java-xpx-dfms-http-api/out-" + path + ".tar", is);
      }

   }

   @Test
   void test02CopyFile() {

   }

   @Test
   void test03MoveFile() {

   }

   @Test
   void test04RemoveFile() throws IOException {
      // remove the content
      drive.remove(FIXED_CID, path+"/text1.txt").timeout(30, TimeUnit.SECONDS).blockingAwait();
      // now try to retrieve the content again
      drive.get(FIXED_CID, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
   }

   @Test
   void test05RemoveAll() throws IOException {
      // remove the content
      drive.remove(FIXED_CID, path).timeout(30, TimeUnit.SECONDS).blockingAwait();
      // now try to retrieve the content again
      assertThrows(RuntimeException.class,
            () -> drive.get(FIXED_CID, path).timeout(30, TimeUnit.SECONDS).blockingFirst());
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
