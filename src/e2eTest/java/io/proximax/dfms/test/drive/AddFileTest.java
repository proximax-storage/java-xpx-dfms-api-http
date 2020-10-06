/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.drive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileSystemException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.DriveItem;
import io.proximax.dfms.model.drive.DriveItemType;
import io.proximax.dfms.model.drive.content.ByteArrayContent;
import io.proximax.dfms.model.drive.content.FileSystemContent;
import io.proximax.dfms.model.drive.content.InputStreamContent;

/**
 * Make sure we can add files
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddFileTest {

   private static final Cid CONTRACT = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");

   private final String path = "hello" + System.currentTimeMillis();// Long.toString(new Random().nextLong());

   private DFMSClient api;
   private DriveRepository drive;

   @BeforeAll
   void init() throws MalformedURLException, FileSystemException {
      api = new DFMSClient(new URL("http://localhost:6366"));
      drive = api.createDriveRepository();
   }

   @Test
   void addFile() throws IOException, InterruptedException {
      DriveContent addContent = new FileSystemContent(
            new File("src/e2eTest/resources/simple/subdir/test_image_file.png").toPath());
      Cid cid = drive.add(CONTRACT, path + ".png", addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      assertFile("", path + ".png");
   }

   @Test
   void addFileToNewDir() throws IOException, InterruptedException {
      DriveContent addContent = new FileSystemContent(
            new File("src/e2eTest/resources/simple/subdir/test_image_file.png").toPath());
      drive.makeDir(CONTRACT, path + "subdir").timeout(30, TimeUnit.SECONDS).blockingAwait();
      Cid cid = drive.add(CONTRACT, path + "subdir/file.png", addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      assertFile(path + "subdir", "file.png");
   }

   @Test
   void addFileAsInputStream() throws FileNotFoundException, IOException {
      try (FileInputStream fis = new FileInputStream("src/e2eTest/resources/simple/subdir/test_image_file.png")) {
         DriveContent content = new InputStreamContent(Optional.of("will be lost"), fis);
         drive.add(CONTRACT, path + "-as-stream.png", content).timeout(30, TimeUnit.SECONDS).blockingFirst();
      }
      assertFile("", path + "-as-stream.png");
   }
   
   @Test
   void addFileAsByteArray() throws FileNotFoundException, IOException {
      try (FileInputStream fis = new FileInputStream("src/e2eTest/resources/simple/subdir/test_image_file.png")) {
         DriveContent content = new ByteArrayContent(Optional.of("will be lost"), IOUtils.toByteArray(fis));
         drive.add(CONTRACT, path + "-as-array.png", content).timeout(30, TimeUnit.SECONDS).blockingFirst();
      }
      assertFile("", path + "-as-array.png");
   }
   
   /**
    * test the file on given path that it matches the image from simple subdir
    * 
    * @param filePath the path where file was placed
    * @param fileName the name of the file on drive
    */
   void assertFile(String filePath, String fileName) {
      DriveItem item = drive.stat(CONTRACT, filePath + "/" + fileName).blockingFirst();
      assertEquals(fileName, item.getName());
      assertEquals(DriveItemType.FILE, item.getType());
      assertEquals(581312l, item.getSize());
      assertEquals(Cid.decode("zdj7Wge9XgaLSfDQx9w5WrsjCKCgt3rHpLTu2bgTzk43B9wtP"), item.getCid());
   }
}
