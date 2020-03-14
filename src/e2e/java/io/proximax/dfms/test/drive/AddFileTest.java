/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.drive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.vfs2.FileSystemException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import io.proximax.cid.Cid;
import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.DriveItem;
import io.proximax.dfms.model.drive.DriveItemType;
import io.proximax.dfms.model.drive.content.FileSystemContent;

/**
 * TODO add proper description
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class AddFileTest {

   private static final Cid CONTRACT = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");

   private final String path = "hello" + System.currentTimeMillis();// Long.toString(new Random().nextLong());

   private StorageApi api;
   private DriveRepository drive;

   @BeforeAll
   void init() throws MalformedURLException, FileSystemException {
      api = new StorageApi(new URL("http://localhost:6366"));
      drive = api.createDriveRepository();
   }

   @Test
   void test01AddFile() throws IOException, InterruptedException {
      DriveContent addContent = new FileSystemContent(
            new File("src/e2e/resources/simple/subdir/test_image_file.png").toPath());
      Cid cid = drive.add(CONTRACT, path + ".png", addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      System.out.println("ID of uploaded data: " + cid);
   }

   @Test
   void test01AddFileToNewDir() throws IOException, InterruptedException {
      DriveContent addContent = new FileSystemContent(
            new File("src/e2e/resources/simple/subdir/test_image_file.png").toPath());
      drive.makeDir(CONTRACT, path + "subdir").timeout(30, TimeUnit.SECONDS).blockingAwait();
      Cid cid = drive.add(CONTRACT, path + "subdir/file.png", addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      System.out.println("ID of uploaded data: " + cid);
   }

   @Test
   void test03StatUploadedFile() {
      DriveItem item = drive.stat(CONTRACT, path + ".png").blockingFirst();
      assertEquals(path + ".png", item.getName());
      assertEquals(DriveItemType.FILE, item.getType());
      assertEquals(581312l, item.getSize());
      assertEquals(Cid.decode("zdj7Wge9XgaLSfDQx9w5WrsjCKCgt3rHpLTu2bgTzk43B9wtP"), item.getCid());
   }
}
