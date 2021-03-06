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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.vfs2.FileSystemException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.DriveServices;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.DriveItem;
import io.proximax.dfms.model.drive.DriveItemType;
import io.proximax.dfms.model.drive.content.DirectoryContent;
import io.proximax.dfms.model.drive.content.FileSystemContent;

/**
 * Test addition of directory to DFMS. either from file-system or as an arbitrary tree structure
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddDirectoryTest {

   private static final Cid CONTRACT = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
   private static final Random RANDOM = new Random(System.nanoTime());

   private DFMSClient api;
   private DriveServices drive;

   @BeforeAll
   void init() throws MalformedURLException, FileSystemException {
      api = new DFMSClient(new URL("http://localhost:6366"));
      drive = api.createDriveServices();
   }

   @Test
   void addDirectoryAsFileSystemContent() throws IOException, InterruptedException {
      final String path = "addDirFs" + Long.toString(RANDOM.nextLong());

      DriveContent addContent = new FileSystemContent(new File("src/e2eTest/resources/simple").toPath());
      Cid cid = drive.add(CONTRACT, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      // check the resulting structure
      assertSimpleContent(path);
//      // remove the content - this kills the server...
//      drive.remove(CONTRACT, path).blockingAwait();
//      final Observable<DriveContent> removedContent = drive.get(CONTRACT, path);
//      assertThrows(DFMSResponseException.class, () -> removedContent.blockingFirst());
   }

   @Test
   void addDirectoryAsDirContent() throws IOException, InterruptedException {
      final String path = "addDirDir" + Long.toString(RANDOM.nextLong());

      DriveContent simpleDirContent = new FileSystemContent(new File("src/e2eTest/resources/simple").toPath());
      DriveContent content = new DirectoryContent(Optional.of("to be lost"), simpleDirContent);
      Cid cid = drive.add(CONTRACT, path, content).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      // check the resulting structure
      assertSimpleContent(path + "/simple");
   }

   private void assertSimpleContent(String path) {
      {
         List<DriveItem> items = drive.ls(CONTRACT, path).blockingFirst();
         assertEquals(4, items.size());
      }
      {
         List<DriveItem> items = drive.ls(CONTRACT, path + "/subdir").blockingFirst();
         assertEquals(1, items.size());
         DriveItem item = items.get(0);
         assertEquals("test_image_file.png", item.getName());
         assertEquals(DriveItemType.FILE, item.getType());
         assertEquals(581312l, item.getSize());
         assertEquals(Cid.decode("zdj7Wge9XgaLSfDQx9w5WrsjCKCgt3rHpLTu2bgTzk43B9wtP"), item.getCid());
      }
      {
         DriveItem item = drive.stat(CONTRACT, path + "/subdir/test_image_file.png").blockingFirst();
         assertEquals("test_image_file.png", item.getName());
         assertEquals(DriveItemType.FILE, item.getType());
         assertEquals(581312l, item.getSize());
         assertEquals(Cid.decode("zdj7Wge9XgaLSfDQx9w5WrsjCKCgt3rHpLTu2bgTzk43B9wtP"), item.getCid());
      }
   }
}
