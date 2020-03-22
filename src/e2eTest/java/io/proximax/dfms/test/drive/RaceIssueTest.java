/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.drive;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.proximax.cid.Cid;
import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.content.FileSystemContent;
import io.proximax.dfms.test.utils.DriveContentUtils;

/**
 * Tests that (used to) break server
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RaceIssueTest {

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
   @Disabled("Disabled by default before server is fixed")
   void killServer() throws IOException {
      DriveContent addContent = new FileSystemContent(new File("src/e2eTest/resources/simple").toPath());
      drive.add(CONTRACT, path, addContent).timeout(30, TimeUnit.SECONDS).blockingFirst();
      drive.remove(CONTRACT, path + "/text1.txt").timeout(30, TimeUnit.SECONDS).blockingAwait();
      // any operation shortly after delete seems to break the server
      drive.remove(CONTRACT, path).timeout(30, TimeUnit.SECONDS).blockingAwait();
   }
}
