/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import io.proximax.dfms.StorageApi;
import io.proximax.dfms.http.HttpRepository.OkHttp3ResponseCallback;
import io.proximax.dfms.model.exceptions.DFMSResponseException;
import io.proximax.dfms.model.exceptions.DFMSRuntimeException;
import io.reactivex.ObservableEmitter;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * {@link HttpRepository} tests
 */
class HttpRepositoryTest {
   private static final String URL = "http://1.2.3.4:2345/";

   private OkHttpClient client;

   @BeforeEach
   void before() {
      client = Mockito.mock(OkHttpClient.class);
   }

   @Test
   void testConstructor() throws MalformedURLException {
      StorageApi api = new StorageApi(new URL(URL));
      HttpRepository<StorageApi> repo = new HttpRepository<>(api, Optional.empty(), client);
      // check initialized fields
      assertEquals(URL, repo.getApiUrl().toString());
      assertEquals(api, repo.getApi());
      assertEquals(client, repo.getClient());
      assertNotNull(repo.getGson());

   }

   @Test
   void testURLNoPathNoSlash() throws MalformedURLException {
      final String API_URL = "http://1.2.3.4:2345";
      StorageApi api = new StorageApi(new URL(API_URL));
      HttpRepository<StorageApi> repo = new HttpRepository<>(api, Optional.empty(), client);
      assertEquals(API_URL + "/", repo.getApiUrl().toString());
   }

   @Test
   void testURLNoPathSlash() throws MalformedURLException {
      StorageApi api = new StorageApi(new URL(URL));
      HttpRepository<StorageApi> repo = new HttpRepository<>(api, Optional.empty(), client);
      assertEquals(URL, repo.getApiUrl().toString());
   }

   @Test
   void testURLWithPath() throws MalformedURLException {
      final String API_URL = "http://1.2.3.4:2345";
      StorageApi api = new StorageApi(new URL(API_URL));
      HttpRepository<StorageApi> repo = new HttpRepository<>(api, Optional.of("api/v1"), client);
      assertEquals(API_URL + "/api/v1", repo.getApiUrl().toString());
   }

   @Test
   void testURLWithAbsolutePath() throws MalformedURLException {
      final String API_URL = "http://1.2.3.4:2345";
      StorageApi api = new StorageApi(new URL(API_URL));
      assertThrows(IllegalArgumentException.class, () -> new HttpRepository<>(api, Optional.of("/api/v1"), client));
   }

   @Test
   void checkUrlBuilderFunctionality() {
      final String urlBase = "http://1.2.3.4:1234/";
      final String urlPath = "api/v9";
      final String urlCommand = "drive/remove";
      final String arg1 = "arg1";
      final String arg2 = "arg 2";
      HttpUrl url = HttpUrl.get(urlBase).newBuilder().addPathSegments(urlPath).addPathSegments(urlCommand)
            .addQueryParameter("arg", arg1).addQueryParameter("arg", arg2).build();

      assertEquals("http://1.2.3.4:1234/api/v9/drive/remove?arg=arg1&arg=arg%202", url.toString());
   }

   @Test
   void checkUrlBuilderForRepo() throws MalformedURLException {
      final String urlBase = "http://1.2.3.4:1234/";
      final String urlPath = "api/v4";
      final String urlCommand = "drive/remove";
      final String arg1 = "arg1";
      final String arg2 = "arg 2";
      // create repo
      StorageApi api = new StorageApi(new URL(urlBase));
      HttpRepository<StorageApi> repo = new HttpRepository<>(api, Optional.of(urlPath), client);

      assertEquals("http://1.2.3.4:1234/api/v4/drive/remove?arg=arg1&arg=arg%202",
            repo.buildUrl(urlCommand, arg1, arg2).toString());
   }

   @Test
   void checkCallback() throws IOException {
      @SuppressWarnings("unchecked")
      ObservableEmitter<Response> emitter = Mockito.mock(ObservableEmitter.class);
      Request request = new Request.Builder().url("http://localhost").build();
      Response response = new Response.Builder().request(request).protocol(Protocol.HTTP_2).code(200).message("hello")
            .build();
      // tested object
      HttpRepository.OkHttp3ResponseCallback callback = new OkHttp3ResponseCallback(emitter);
      // invoke onResponse callback
      callback.onResponse(null, response);
      Mockito.verify(emitter, Mockito.times(1)).onNext(response);
      Mockito.verify(emitter, Mockito.times(1)).onComplete();
      Mockito.verify(emitter, Mockito.times(0)).onError(Matchers.any());
      // invoke onFailure callback
      IOException exc = Mockito.mock(IOException.class);
      callback.onFailure(null, exc);
      Mockito.verify(emitter, Mockito.times(1)).onError(exc);
   }

   @Test
   void checkMapBody() throws IOException {
      Request request = new Request.Builder().url("http://localhost").build();
      Response response = new Response.Builder().request(request).protocol(Protocol.HTTP_2).code(200).message("hello")
            .body(ResponseBody.create(MediaType.get("text/plain"), "this is the body")).build();
      HttpRepository<StorageApi> repo = new HttpRepository<>(new StorageApi(new URL(URL)), Optional.empty(), client);

      String body = repo.mapStringOrError(response);
      assertEquals("this is the body", body);
   }

   @Test
   void checkMapError199StringBody() throws IOException {
      Request request = new Request.Builder().url("http://localhost").build();
      Response response = new Response.Builder().request(request).protocol(Protocol.HTTP_2).code(199).message("hello")
            .body(ResponseBody.create(MediaType.get("text/plain"), "this is the body")).build();
      HttpRepository<StorageApi> repo = new HttpRepository<>(new StorageApi(new URL(URL)), Optional.empty(), client);

      assertThrows(DFMSRuntimeException.class, () -> repo.mapRespBodyOrError(response));
   }

   @Test
   void checkMapError300StringBody() throws IOException {
      Request request = new Request.Builder().url("http://localhost").build();
      Response response = new Response.Builder().request(request).protocol(Protocol.HTTP_2).code(300).message("hello")
            .body(ResponseBody.create(MediaType.get("text/plain"), "this is the body")).build();
      HttpRepository<StorageApi> repo = new HttpRepository<>(new StorageApi(new URL(URL)), Optional.empty(), client);

      assertThrows(DFMSRuntimeException.class, () -> repo.mapRespBodyOrError(response));
   }

   @Test
   void checkMapErrorJsonBody() throws IOException {
      Request request = new Request.Builder().url("http://localhost").build();
      Response response = new Response.Builder()
            .request(request).protocol(Protocol.HTTP_2).code(150).message("hello").body(ResponseBody
                  .create(MediaType.get("text/plain"), "{\"Message\"=\"msg\", \"Code\"=\"2\", \"Type\"=\"tp\"}"))
            .build();
      HttpRepository<StorageApi> repo = new HttpRepository<>(new StorageApi(new URL(URL)), Optional.empty(), client);

      assertThrows(DFMSResponseException.class, () -> repo.mapRespBodyOrError(response));
   }
}
