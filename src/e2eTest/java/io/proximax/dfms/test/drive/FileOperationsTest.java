/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.drive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.DriveItem;
import io.proximax.dfms.model.drive.DriveItemType;
import io.proximax.dfms.model.drive.content.FileSystemContent;
import io.proximax.dfms.model.exceptions.DFMSResponseException;
import io.proximax.dfms.test.utils.DriveContentUtils;

/**
 * Test file/directory operations like copy, move, mkdir
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class FileOperationsTest {

   private static final Cid CONTRACT = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");

   private final String path = "hello" + System.currentTimeMillis();// Long.toString(new Random().nextLong());

   private DFMSClient api;
   private DriveRepository drive;
   private DefaultFileSystemManager fsManager;

   @BeforeAll
   void init() throws MalformedURLException, FileSystemException {
      api = new DFMSClient(new URL("http://localhost:6366"));
      drive = api.createDriveRepository();
      // file system manager for access to retrieved tar-balls
      fsManager = DriveContentUtils.createFSManager();
   }

   @AfterAll
   void cleanup() {
      fsManager.close();
   }

   @Test
   void test01AddDirectory() throws IOException, InterruptedException {
      DriveContent addContent = new FileSystemContent(new File("src/e2eTest/resources/simple").toPath());
      Cid cid = drive.add(CONTRACT, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertNotNull(cid);
      System.out.println("ID of uploaded data: " + cid);
      // now make request for that data
      DriveContent content = drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
      // test the contents
      FileObject rootDir = DriveContentUtils.openContent(fsManager, content, path);
      assertEquals(path, rootDir.getName().getBaseName());
      assertEquals(4, rootDir.getChildren().length);
      FileObject subDir = rootDir.getChild("subdir");
      assertEquals(1, subDir.getChildren().length);
   }

   @Test
   void test02CopyFile() throws IOException {
      String targetPath = path + "copy_dst";
      drive.copy(CONTRACT, path, targetPath).timeout(30, TimeUnit.SECONDS).blockingAwait();
      // assert the target
      DriveItem source = drive.stat(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
      DriveItem target = drive.stat(CONTRACT, targetPath).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertEquals(source.getSize(), target.getSize());
      assertEquals(source.getType(), target.getType());
      assertEquals(source.getCid(), target.getCid());
      assertEquals(targetPath, target.getName());
      assertEquals(path, source.getName());
   }

   @Test
   void test03MoveFile() throws IOException {
      // store current source
      DriveItem source = drive.stat(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
      // make the move
      String targetPath = path + "move_dst";
      drive.move(CONTRACT, path, targetPath).timeout(30, TimeUnit.SECONDS).blockingAwait();
      // make sure source no longer exists
      assertThrows(DFMSResponseException.class,
            () -> drive.stat(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst());
      // assert the target
      DriveItem target = drive.stat(CONTRACT, targetPath).timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertEquals(source.getSize(), target.getSize());
      assertEquals(source.getType(), target.getType());
      assertEquals(source.getCid(), target.getCid());
      assertEquals(targetPath, target.getName());
      assertEquals(path, source.getName());
      // move the stuff back
      drive.move(CONTRACT, targetPath, path).timeout(30, TimeUnit.SECONDS).blockingAwait();
      // make sure target no longer exists
      assertThrows(DFMSResponseException.class,
            () -> drive.stat(CONTRACT, targetPath).timeout(30, TimeUnit.SECONDS).blockingFirst());

   }

   @Test
   void test04Mkdir() throws IOException {
      // remove the content
      drive.makeDir(CONTRACT, path + "somedir").timeout(30, TimeUnit.SECONDS).blockingAwait();
      // now try to stat the directory
      DriveItem target = drive.stat(CONTRACT, path + "somedir").timeout(30, TimeUnit.SECONDS).blockingFirst();
      assertEquals(path + "somedir", target.getName());
      assertEquals(DriveItemType.DIRECTORY, target.getType());
      assertEquals(0, target.getSize());
   }

   @Test
   @Disabled("Running this breaks storage server until restart........")
   void test04RemoveFile() throws IOException {
      // remove the content
      drive.remove(CONTRACT, path + "/text1.txt").timeout(30, TimeUnit.SECONDS).blockingAwait();
      // now try to retrieve the content again
      drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst();
   }

   @Test
   @Disabled("Running this breaks storage server until restart........")
   void test05RemoveAll() throws IOException, InterruptedException {
      // remove the content
      drive.remove(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingAwait();
      // now try to retrieve the content again
      assertThrows(RuntimeException.class,
            () -> drive.get(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingFirst());
   }
}
