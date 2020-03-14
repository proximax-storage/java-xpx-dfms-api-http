/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.drive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.junit.jupiter.api.AfterAll;
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
import io.proximax.dfms.test.utils.DriveContentUtils;

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
   void killServer() throws IOException {
      DriveContent addContent = new FileSystemContent(new File("src/e2e/resources/simple").toPath());
      drive.add(CONTRACT, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      drive.remove(CONTRACT, path + "/text1.txt").timeout(30, TimeUnit.SECONDS).blockingAwait();
      drive.remove(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingAwait();
   }

   @Test
   void test01AddDirectory() throws IOException, InterruptedException {
      DriveContent addContent = new FileSystemContent(new File("src/e2e/resources/simple").toPath());
      Cid cid = drive.add(CONTRACT, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      System.out.println("ID of uploaded data: " + cid);
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
      // now try adding the directory as subdirectory of original upload
      {
         drive.add(CONTRACT, path+"/subdir2", new FileSystemContent(new File("src/e2e/resources/simple").toPath())).timeout(30, TimeUnit.SECONDS).blockingFirst();
         DriveContent content = drive.get(CONTRACT, path+"/subdir").timeout(30, TimeUnit.SECONDS).blockingFirst();
         // test the contents
//         DriveContentUtils.writeToFile(content, new File("subdirtest.tar"));
//         FileObject rootDir = DriveContentUtils.openContent(fsManager, content, path);
//         assertEquals(path, rootDir.getName().getBaseName());
//         assertEquals(4, rootDir.getChildren().length);
//         FileObject subDir = rootDir.getChild("subdir");
//         assertEquals(1, subDir.getChildren().length);
      }
      {
         List<DriveItem> items = drive.ls(CONTRACT, path+"/subdir").blockingFirst();
         assertEquals(1, items.size());
         DriveItem item = items.get(0);
         assertEquals("test_image_file.png", item.getName());
         assertEquals(DriveItemType.FILE, item.getType());
         assertEquals(581312l, item.getSize());
         assertEquals(Cid.decode("zdj7Wge9XgaLSfDQx9w5WrsjCKCgt3rHpLTu2bgTzk43B9wtP"), item.getCid());
      }
      {
         DriveItem item = drive.stat(CONTRACT, path+"/subdir/test_image_file.png").blockingFirst();
         assertEquals("test_image_file.png", item.getName());
         assertEquals(DriveItemType.FILE, item.getType());
         assertEquals(581312l, item.getSize());
         assertEquals(Cid.decode("zdj7Wge9XgaLSfDQx9w5WrsjCKCgt3rHpLTu2bgTzk43B9wtP"), item.getCid());
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
}
