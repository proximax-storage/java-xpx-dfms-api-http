/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import io.proximax.dfms.exception.DFMSRuntimeException;

/**
 * Utility methods for manipulation with HTTP data
 */
public class HttpUtils {
   private static final String UTF_8 = StandardCharsets.UTF_8.name();

   private HttpUtils() {
      throw new AssertionError("Not to be instantiated");
   }

   /**
    * encode provided text to be URL-safe
    * 
    * @param text the text to encode
    * @return encoded text
    */
   public static String encode(String text) {
      try {
         return URLEncoder.encode(text, UTF_8);
      } catch (UnsupportedEncodingException e) {
         throw new DFMSRuntimeException("Failed to encode the file name", e);
      }
   }
}
