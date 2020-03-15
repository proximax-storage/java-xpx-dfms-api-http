/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.drive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.cid.Cid;
import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.content.FileSystemContent;
import io.proximax.dfms.test.utils.DriveContentUtils;

/**
 * retrieve actual data from the DFMS
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetDataTest {

   private static final Cid CONTRACT = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");

   private final String path = "hello" + System.currentTimeMillis();// Long.toString(new Random().nextLong());

   private StorageApi api;
   private DriveRepository drive;
   private DefaultFileSystemManager fsManager;

   @BeforeAll
   void init() throws MalformedURLException, FileSystemException {
      api = new StorageApi(new URL("http://localhost:6366"));
      drive = api.createDriveRepository();
      // file system manager for access to retrieved tar-balls
      fsManager = DriveContentUtils.createFSManager();
   }

   @AfterAll
   void cleanup() {
      fsManager.close();
   }

   @Test
   void checkDirectory() throws IOException, InterruptedException {
      DriveContent addContent = new FileSystemContent(new File("src/e2e/resources/simple").toPath());
      drive.add(CONTRACT, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      // now make request for that data
      {
         DriveContent content = drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
         // test the contents
         FileObject rootDir = DriveContentUtils.openContent(fsManager, content, path);
         assertEquals(path, rootDir.getName().getBaseName());
         assertEquals(4, rootDir.getChildren().length);
         FileObject subDir = rootDir.getChild("subdir");
         assertEquals(1, subDir.getChildren().length);
      }
      // test sub-paths
      {
         DriveContent content = drive.get(CONTRACT, path + "/subdir/").timeout(30, TimeUnit.SECONDS).blockingFirst();
         FileObject rootDir = DriveContentUtils.openContent(fsManager, content, "subdir");
         assertEquals(1, rootDir.getChildren().length);
      }
      // direct query for file
      {
         // make request to a path of the image file
         DriveContent content = drive.get(CONTRACT, path + "/subdir/test_image_file.png").timeout(30, TimeUnit.SECONDS)
               .blockingFirst();
         // response is tar with the image in the root directory so open the image
         FileObject image = DriveContentUtils.openContent(fsManager, content, "test_image_file.png");
         // make sure we have the file
         assertTrue(image.isFile());
         assertEquals("test_image_file.png", image.getName().getBaseName());
      }
   }
}
