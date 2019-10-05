/*
 * Copyright 2019 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.proximax.dfms.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import com.google.gson.JsonObject;

import io.proximax.dfms.ServiceNode;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Generic HTTP client implementation using OK HTTP3
 */
public class HttpClientOkHttp3 implements HttpClient {
   private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

   private final URI apiUri;
   private final OkHttpClient client;

   /**
    * create new HTTP client for specified API
    * 
    * @param node the node we are working with
    * @param apiPath the path to the API on the node
    */
   public HttpClientOkHttp3(ServiceNode node, Optional<String> apiPath) {
      // build the API URI for the node
      try {
         this.apiUri = node.getUrl().toURI().resolve(apiPath.orElse(""));
      } catch (URISyntaxException e) {
         throw new IllegalStateException("Failed to construct repository URI", e);
      }
      // create new instance of the HTTP client
      client = new OkHttpClient();
   }

   @Override
   public Observable<HttpResponse> get(String path) {
      String absoluteUrl = apiUri.resolve(path).toASCIIString();
      return getAbs(absoluteUrl);
   }

   @Override
   public Observable<HttpResponse> getAbs(String absoluteUrl) {
      final Request request = new Request.Builder().get().url(absoluteUrl).build();
      return createHttpResponseObservable(request);
   }

   @Override
   public Observable<HttpResponse> post(String path, JsonObject jsonObject) {
      String absoluteUrl = apiUri.resolve(path).toASCIIString();
      return postAbs(absoluteUrl, jsonObject);

   }

   @Override
   public Observable<HttpResponse> put(String path, JsonObject jsonObject) {
      String absoluteUrl = apiUri.resolve(path).toASCIIString();
      return putAbs(absoluteUrl, jsonObject);
   }

   @Override
   public Observable<HttpResponse> postAbs(String absoluteUrl, JsonObject jsonObject) {
      RequestBody body;
      if (jsonObject == null) {
          body = RequestBody.create(null, new byte[]{});
      } else {
         body = RequestBody.create(JSON, jsonObject.toString());
      }
      final Request request = new Request.Builder().post(body).url(absoluteUrl).build();
      return createHttpResponseObservable(request);
   }

   @Override
   public Observable<HttpResponse> putAbs(String absoluteUrl, JsonObject jsonObject) {
      RequestBody body = RequestBody.create(JSON, jsonObject.toString());
      final Request request = new Request.Builder().put(body).url(absoluteUrl).build();
      return createHttpResponseObservable(request);
   }


   @Override
   public WebSocket newWebSocket(Request request, WebSocketListener webSocketListener) {
      return client.newWebSocket(request, webSocketListener);
   }
   
   /**
    * use okhttp client to execute provided request and return Observable for the response
    * 
    * @param request the HTTP request to execute
    * @return observable response
    */
   private Observable<HttpResponse> createHttpResponseObservable(Request request) {
      return Observable.create(emitter -> {
         try {
            Response response = client.newCall(request).execute();
            emitter.onNext(new OkHttpResponse(response));
            emitter.onComplete();
         } catch (IOException e) {
            emitter.onError(e);
         }
      });
   }

   /**
    * HTTP response implementation for OK HTTP3
    */
   private static class OkHttpResponse implements HttpResponse {
      final Response response;

      OkHttpResponse(Response response) {
         this.response = response;
      }

      @Override
      public int getCode() {
         return response.code();
      }

      @Override
      public String getStatusMessage() {
         return response.message();
      }

      @Override
      public String getBodyString() throws IOException {
         ResponseBody body = response.body();
         if (body == null) {
            return null;
         }
         return body.string();
      }
   }
}