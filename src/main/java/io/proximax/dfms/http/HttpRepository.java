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

/**
 * Generic HTTP repository implementation, keeping track of the node, HTTP client and mapper
 */
public class HttpRepository <T extends ServiceNode> {
   protected static final String SLASH = "/";
   
   private final T api;
   private final HttpClient client;
   private final Gson gson;

   /**
    * create and initialize new instance for specified API
    * 
    * @param api the main API
    * @param apiPath the path to the API on the node
    */
   protected HttpRepository(T api, Optional<String> apiPath) {
      Validate.notNull(api, "api has to be provided");
      this.api = api;
      this.client = new HttpClientOkHttp3(api, apiPath);
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
   public HttpClient getClient() {
      return client;
   }

   /**
    * @return the gson
    */
   public Gson getGson() {
      return gson;
   }

   /**
    * throw RuntimeException on error or return body of the response
    * 
    * @param response response to examine
    * @return body of the response as string
    */
   public static String mapStringOrError(final HttpResponse response) {
      if (response.getCode() < 200 || response.getCode() > 299) {
         throw new RuntimeException(response.getCode() + " " + response.getStatusMessage());
      }
      try {
         return response.getBodyString();
      } catch (IOException e) {
         throw new RuntimeException(e.getMessage());
      }
   }
}
