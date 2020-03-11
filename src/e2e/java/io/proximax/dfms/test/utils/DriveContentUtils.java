/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.CacheStrategy;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.cache.SoftRefFilesCache;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.provider.local.DefaultLocalFileProvider;
import org.apache.commons.vfs2.provider.tar.TarFileProvider;

import io.proximax.dfms.drive.DriveContent;

/**
 * TODO add proper description
 */
public class DriveContentUtils {
   public static DefaultFileSystemManager createFSManager() throws FileSystemException {
      // create empty instance
      DefaultFileSystemManager fsManager = new DefaultFileSystemManager();
      // configure cache
      fsManager.setFilesCache(new SoftRefFilesCache());
      fsManager.setCacheStrategy(CacheStrategy.ON_RESOLVE);
      // add providers
      fsManager.addProvider("file", new DefaultLocalFileProvider());
      fsManager.addProvider("tgz", new TarFileProvider());
      fsManager.addProvider("tar", new TarFileProvider());
      // initialize
      fsManager.init();
      return fsManager;
   }

   public static File createTempTar(String name, DriveContent content) throws IOException {
      File tempFile = File.createTempFile(name, ".tar");
      writeToFile(content, tempFile);
      return tempFile;
   }

   /**
    * open the drive content as virtual file system
    * 
    * @param fsManager file system manager. See {@link #createFSManager()} on how to get one
    * @param content the drive content instance retrieved from the server
    * @param rootPath the root path of the data
    * @return the file object representing the data on the root path
    * @throws IOException
    */
   public static FileObject openContent(FileSystemManager fsManager, DriveContent content, String rootPath)
         throws IOException {
      File tempFile = DriveContentUtils.createTempTar("proximax-dfms-" + rootPath, content);
      return fsManager.resolveFile("tar:file://" + tempFile.getCanonicalPath() + "!/" + rootPath);
   }
   
   /**
    * write the content to a file
    * 
    * @param content the drive content to be written to disk
    * @param target target file that will receive the data
    * @throws IOException
    */
   public static void writeToFile(DriveContent content, File target) throws IOException {
      try (InputStream is = content.getInputStream()) {
         FileUtils.copyToFile(is, target);
      }
   }
   
}
