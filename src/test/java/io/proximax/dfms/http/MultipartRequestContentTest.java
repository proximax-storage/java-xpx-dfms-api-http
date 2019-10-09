/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.proximax.dfms.drive.DriveContent;
import io.proximax.dfms.drive.FileSystemContent;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;

/**
 * TODO add proper description
 */
class MultipartRequestContentTest {

   @Test
   @Disabled("not really a test")
   void test() throws IOException {
      DriveContent content = new FileSystemContent(Paths.get(".", "src", "test", "resources", "simple"));
      RequestBody body = new MultipartRequestContent(content);
      BufferedSink sink = new Buffer();
      body.writeTo(sink);
      System.out.println(sink.toString());
   }

}
