/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.utils;

import java.nio.file.Path;

/**
 * Utility methods for work with file system
 */
public class FileSystemUtils {

   private FileSystemUtils() {
      throw new AssertionError("Not to be instantiated");
   }

   /**
    * encode path to be safe for URLs
    * 
    * @param path the path to use
    * @return encoded file name
    */
   public static String encode(Path path) {
      return HttpUtils.encode(path.toString());
   }

}
