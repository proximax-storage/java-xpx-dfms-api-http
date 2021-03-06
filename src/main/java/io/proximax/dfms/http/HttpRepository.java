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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.proximax.dfms.ServiceNode;
import io.proximax.dfms.gen.model.ErrResult;
import io.proximax.dfms.model.exceptions.DFMSResponseException;
import io.proximax.dfms.model.exceptions.DFMSRuntimeException;
import io.proximax.dfms.model.exceptions.ResponseErrorType;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Generic HTTP repository implementation, keeping track of the node, HTTP client and mapper
 */
public class HttpRepository<T extends ServiceNode> {
   protected static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
   protected static final MediaType MEDIA_STREAM = MediaType.parse("application/octet-stream");
   protected static final MediaType MEDIA_DIRECTORY = MediaType.parse("application/x-directory");

   protected static final String QUERY_PARAM_ARG = "arg";

   private final T api;
   private final HttpUrl apiUrl;
   private final OkHttpClient client;
   private final OkHttpClient longPollingClient;
   private final Gson gson;

   /**
    * create and initialize new instance for specified API
    * 
    * @param api the main API
    * @param apiPath the path to the API on the node
    * @param client the HTTP client to use for requests
    */
   protected HttpRepository(T api, Optional<String> apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      Validate.notNull(api, "api has to be provided");
      Validate.notNull(apiPath, "apiPath is mandatory");
      Validate.isTrue(!apiPath.orElse("").startsWith("/"), "apiPath can not start by / and has to be relative");
      this.api = api;
      // build the API URL for the node
      this.apiUrl = HttpUrl.get(api.getUrl()).newBuilder().addPathSegments(apiPath.orElse("")).build();
      // validate the URL
      Validate.notNull(apiUrl, "Only HTTP/S URLs are supported");
      // keep clients
      this.client = client;
      this.longPollingClient = longPollingClient;
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
    * @return the longPollingClient
    */
   public OkHttpClient getLongPollingClient() {
      return longPollingClient;
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
    * @param arguments collection of arguments what will be part of the URL
    * @return the URL builder
    */
   protected HttpUrl.Builder buildUrl(String command, String... arguments) {
      return buildUrl(command, new HashMap<>(), arguments);
   }

   /**
    * get URL builder for specified command representing relative URL segments
    * 
    * @param command relative URL path segments (e.g. drive/remove)
    * @param options explicit key-value pairs for query parameters
    * @param arguments collection of arguments what will be part of the URL
    * @return the URL builder
    */
   protected HttpUrl.Builder buildUrl(String command, Map<String, String> options, String... arguments) {
      HttpUrl.Builder builder = getApiUrl().newBuilder().addPathSegments(command);
      for (String argument : arguments) {
         builder.addQueryParameter(QUERY_PARAM_ARG, argument);
      }
      for (Map.Entry<String, String> entry : options.entrySet()) {
         builder.addQueryParameter(entry.getKey(), entry.getValue());
      }
      return builder;
   }

   /**
    * provide observable response for the request
    * 
    * @param request the request to make
    * @return observable response
    */
   protected Observable<Response> makeRequest(Request request, boolean longPolling) {
      OkHttpClient clientForRequest = longPolling ? getLongPollingClient() : getClient();
      final Call call = clientForRequest.newCall(request);
      return Observable.create(emitter -> call.enqueue(new OkHttp3ResponseCallback(emitter)));
   }

   /**
    * throw RuntimeException on error or return body of the response
    * 
    * @param response response to examine
    * @return body of the response as string
    */
   public ResponseBody mapRespBodyOrError(final Response response) {
      final int code = response.code();
      if (code < 200 || code > 299) {
         throw createResponseException(response);
      }
      return response.body();
   }

   /**
    * throw RuntimeException on error or return body of the response
    * 
    * @param response response to examine
    * @return body of the response as string
    */
   public String mapStringOrError(final Response response) {
      try (ResponseBody body = mapRespBodyOrError(response)) {
         return body.string();
      } catch (IOException e) {
         throw new DFMSRuntimeException("Failed to read response body", e);
      }
   }

   /**
    * create observable on response body that emits new string for every line that is appended to the body
    * 
    * @param respBody response body
    * @return observable that emits string for every line of response body content stream
    */
   protected static Observable<String> longPollingObserver(ResponseBody respBody) {
      return Observable.create(emitter -> {
         try (BufferedReader reader = new BufferedReader(respBody.charStream())) {
            // read lines while available
            String line;
            while ((line = reader.readLine()) != null) {
               emitter.onNext(line);
            }
            // emit the complete event to indicate we are done
            emitter.onComplete();
         } catch (IOException | RuntimeException err) {
            // emit any error that might have occurred
            emitter.onError(err);
         }
      });
   }

   /**
    * create exception representing the response error
    * 
    * @param response the response to process
    * @return the exception
    */
   private DFMSRuntimeException createResponseException(final Response response) {
      final int responseCode = response.code();
      final String responseMessage = response.message();
      try {
         final String responseBody = response.body().string();
         return createExceptionFromResponseBody(responseBody);
      } catch (IOException e) {
         return new DFMSRuntimeException(responseCode + "/" + responseMessage + " - missing response body", e);
      }
   }

   /**
    * process response body and create exception from the body content
    * 
    * @param responseBody response body text
    * @return the exception that will be raised to observers
    */
   protected DFMSRuntimeException createExceptionFromResponseBody(String responseBody) {
      try {
         // parse the error DTO from the response
         ErrResult err = getGson().fromJson(responseBody, ErrResult.class);
         // throw error if response did not have code
         if (err.getCode() == null) {
            throw new DFMSRuntimeException("response is missing error code");
         }
         return new DFMSResponseException(ResponseErrorType.getByCode(err.getCode().getValue()), err.getMessage());
      } catch (JsonSyntaxException | DFMSRuntimeException e) {
         return new DFMSRuntimeException("Failed to process response - " + responseBody, e);
      }
   }

   /**
    * POST request with response content
    * 
    * @param url we are making the GET request to
    * @return observable for the action
    */
   protected Observable<Response> makePostObservable(HttpUrl url, boolean longPolling) {
      RequestBody body = RequestBody.create(new byte[0]);
      Request request = new Request.Builder().url(url).post(body).build();
      return makeRequest(request, longPolling);
   }

   /**
    * GET request with response content
    * 
    * @param url we are making the GET request to
    * @return observable for the action
    */
   protected Observable<Response> makeGetObservable(HttpUrl url, boolean longPolling) {
      Request request = new Request.Builder().url(url).build();
      return makeRequest(request, longPolling);
   }

   /**
    * response callback which forwards events to observable emitter
    */
   static class OkHttp3ResponseCallback implements Callback {
      private final ObservableEmitter<Response> emitter;

      public OkHttp3ResponseCallback(ObservableEmitter<Response> emitter) {
         super();
         this.emitter = emitter;
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
         emitter.onNext(response);
         emitter.onComplete();
      }

      @Override
      public void onFailure(Call call, IOException e) {
         emitter.onError(e);
      }
   }
}
