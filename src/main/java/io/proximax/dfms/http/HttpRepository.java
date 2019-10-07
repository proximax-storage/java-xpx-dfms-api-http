/*
 * Copyright 2018 NEM
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
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.gson.Gson;

import io.proximax.dfms.ServiceNode;
import io.proximax.dfms.drive.DriveContent;
import io.reactivex.Observable;
import okhttp3.*;

/**
 * Generic HTTP repository implementation, keeping track of the node, HTTP client and mapper
 */
public class HttpRepository<T extends ServiceNode> {
   protected static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
   protected static final MediaType MEDIA_STREAM = MediaType.parse("application/octet-stream");
   
   protected static final String QUERY_PARAM_ARG = "arg";

   private final T api;
   private final HttpUrl apiUrl;
   private final OkHttpClient client;
   private final Gson gson;

   /**
    * create and initialize new instance for specified API
    * 
    * @param api the main API
    * @param apiPath the path to the API on the node
    * @param client the HTTP client to use for requests
    */
   protected HttpRepository(T api, Optional<String> apiPath, OkHttpClient client) {
      Validate.notNull(api, "api has to be provided");
      Validate.notNull(apiPath, "apiPath is mandatory");
      Validate.isTrue(!apiPath.orElse("").startsWith("/"), "apiPath can not start by / and has to be relative");
      this.api = api;
      // build the API URL for the node
      this.apiUrl = HttpUrl.get(api.getUrl()).newBuilder().addPathSegments(apiPath.orElse("")).build();
      // validate the URL
      Validate.notNull(apiUrl, "Only HTTP/S URLs are supported");
      // create the client
      this.client = client;
      // gson instance
      this.gson = new Gson();
   }

   /**
    * @return the API
    */
   public T getApi() {
      return api;
   }

   /**
    * @return the client
    */
   public OkHttpClient getClient() {
      return client;
   }

   /**
    * @return the gson
    */
   public Gson getGson() {
      return gson;
   }

   /**
    * @return the apiUrl
    */
   public HttpUrl getApiUrl() {
      return apiUrl;
   }

   /**
    * get URL builder for specified command representing relative URL segments
    * 
    * @param command relative URL path segments (e.g. drive/remove)
    * @return the url builder
    */
   protected HttpUrl.Builder buildUrl(String command, String ... arguments) {
      HttpUrl.Builder builder = getApiUrl().newBuilder().addPathSegments(command);
      for (String argument: arguments) {
         builder.addQueryParameter(QUERY_PARAM_ARG, argument);
      }
      return builder;
   }
   
   /**
    * provide observable response for the request
    * 
    * @param request the request to make
    * @return observable response
    */
   protected Observable<Response> makeRequest(Request request) {
      return Observable.create(emitter -> {
         getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               emitter.onNext(response);
               emitter.onComplete();
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
               emitter.onError(e);
            }
         });
      });
   }
   
   /**
    * add drive content as a multi-part form to the request builder
    * 
    * @param bodyBuilder
    * @param name
    * @param fileName
    * @param content
    * @throws IOException
    */
   protected static void addFormContent(MultipartBody.Builder bodyBuilder, String name, String fileName, DriveContent content) throws IOException {
      if (content.isNode()) {
         for (DriveContent child: content.getChildren()) {
            String fname = child.getName().orElse("filedef");
            addFormContent(bodyBuilder, fname, fname, child);
         }
      } else {
         bodyBuilder.addFormDataPart(name, fileName,
               RequestBody.create(MEDIA_STREAM, content.toByteArray()));
      }
   }
   
   /**
    * throw RuntimeException on error or return body of the response
    * 
    * @param response response to examine
    * @return body of the response as string
    */
   public static String mapStringOrError(final Response response) {
      final int code = response.code();
      if (code < 200 || code > 299) {
         throw new RuntimeException(code + " " + response.message());
      }
      try (ResponseBody body = response.body()) {
         return body.string();
      } catch (IOException e) {
         throw new RuntimeException(e.getMessage());
      }
   }
}
