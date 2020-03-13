/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.drive;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.model.drive.content.FileSystemContent;

/**
 * TODO add proper description
 */
class FileSystemContentTest {

   @Test
   void test() throws IOException {
      File f = File.createTempFile("mytest", ".la");
      FileSystemContent fsc = new FileSystemContent(null);
   }

}
