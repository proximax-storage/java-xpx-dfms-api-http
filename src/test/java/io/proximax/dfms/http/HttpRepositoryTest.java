/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.StorageApi;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * TODO add proper description
 */
class HttpRepositoryTest {

   @Test
   void testURLNoPathNoSlash() throws MalformedURLException {
      final String API_URL = "http://1.2.3.4:2345";
      StorageApi api = new StorageApi(new URL(API_URL));
      HttpRepository<StorageApi> repo = new HttpRepository<StorageApi>(api, Optional.empty(), new OkHttpClient());
      assertEquals(API_URL + "/", repo.getApiUrl().toString());
   }

   @Test
   void testURLNoPathSlash() throws MalformedURLException {
      final String API_URL = "http://1.2.3.4:2345/";
      StorageApi api = new StorageApi(new URL(API_URL));
      HttpRepository<StorageApi> repo = new HttpRepository<StorageApi>(api, Optional.empty(), new OkHttpClient());
      assertEquals(API_URL, repo.getApiUrl().toString());
   }

   @Test
   void testURLWithPath() throws MalformedURLException {
      final String API_URL = "http://1.2.3.4:2345";
      StorageApi api = new StorageApi(new URL(API_URL));
      HttpRepository<StorageApi> repo = new HttpRepository<StorageApi>(api, Optional.of("api/v1"), new OkHttpClient());
      assertEquals(API_URL + "/api/v1", repo.getApiUrl().toString());
   }

   @Test
   void checkUrlBuilder() {
      final String urlBase = "http://1.2.3.4:1234/";
      final String urlPath = "api/v1";
      final String urlCommand = "drive/remove";
      final String arg1 = "arg1";
      final String arg2 = "arg 2";
      HttpUrl url = HttpUrl.get(urlBase).newBuilder().addPathSegments(urlPath).addPathSegments(urlCommand)
            .addQueryParameter("arg", arg1).addQueryParameter("arg", arg2).build();

      assertEquals("http://1.2.3.4:1234/api/v1/drive/remove?arg=arg1&arg=arg%202", url.toString());
   }
}
