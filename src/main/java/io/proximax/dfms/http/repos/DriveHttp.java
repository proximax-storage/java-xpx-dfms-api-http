/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import static io.proximax.dfms.utils.HttpUtils.encode;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import io.proximax.dfms.DriveServices;
import io.proximax.dfms.ServiceBase;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.gen.model.CidWrap;
import io.proximax.dfms.gen.model.StatListWrap;
import io.proximax.dfms.gen.model.StatWrap;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.http.MultipartRequestContent;
import io.proximax.dfms.http.RawMultipartRequestContent;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.DriveItem;
import io.proximax.dfms.model.drive.content.InputStreamContent;
import io.proximax.dfms.model.drive.content.RawInputStreamContent;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Drive repository implementation using HTTP protocol
 */
public class DriveHttp extends HttpRepository<ServiceBase> implements DriveServices {

   private static final String URL_ADD = "drive/add";
   private static final String URL_GET = "drive/get";
   private static final String URL_FILE = "drive/file";
   private static final String URL_REMOVE = "drive/rm";
   private static final String URL_MOVE = "drive/mv";
   private static final String URL_COPY = "drive/cp";
   private static final String URL_MKDIR = "drive/mkdir";
   private static final String URL_LS = "drive/ls";
   private static final String URL_STAT = "drive/stat";
   private static final String URL_FLUSH = "drive/flush";

   /**
    * create new instance
    * 
    * @param api the storage API
    * @param apiPath the path to the API on the node
    * @param client the HTTP client to be used to execute requests
    */
   public DriveHttp(ServiceBase api, String apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      super(api, Optional.of(apiPath), client, longPollingClient);
   }

   @Override
   public Observable<Cid> add(Cid id, String path, DriveContent content) throws IOException {
      HttpUrl url = buildUrl(URL_ADD, encode(id), path).build();
      Request request = new Request.Builder().url(url).post(createRequestBody(content)).build();
      // make the request
      return makeRequest(request, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, CidWrap.class))
            .map(CidWrap::getId)
            .map(Cid::decode);
   }

   @Override
   public Observable<DriveContent> get(Cid id, String path) {
      HttpUrl url = buildUrl(URL_GET, encode(id), path).build();
      // caller is responsible to call close on the input stream
      return makeGetObservable(url, false)
            .map(this::mapRespBodyOrError)
            .map(resp -> new InputStreamContent(Optional.empty(), resp.byteStream()));
   }

   @Override
   public Observable<DriveContent> file(Cid id, Cid file) {
      HttpUrl url = buildUrl(URL_FILE, encode(id), encode(file)).build();
      // caller is responsible to call close on the input stream
      return makeGetObservable(url, false)
            .map(this::mapRespBodyOrError)
            .map(resp -> new InputStreamContent(Optional.empty(), resp.byteStream()));
   }

   @Override
   public Completable remove(Cid id, String path) {
      HttpUrl url = buildUrl(URL_REMOVE, encode(id), path).build();
      return makePostObservable(url, false).map(this::mapStringOrError).ignoreElements();
   }

   @Override
   public Completable move(Cid id, String sourcePath, String destinationPath) {
      HttpUrl url = buildUrl(URL_MOVE, encode(id), sourcePath, destinationPath).build();
      return makePostObservable(url, false).map(this::mapStringOrError).ignoreElements();
   }

   @Override
   public Completable copy(Cid id, String sourcePath, String destinationPath) {
      HttpUrl url = buildUrl(URL_COPY, encode(id), sourcePath, destinationPath).build();
      return makePostObservable(url, false).map(this::mapStringOrError).ignoreElements();
   }

   @Override
   public Completable makeDir(Cid id, String path) {
      HttpUrl url = buildUrl(URL_MKDIR, encode(id), path).build();
      return makePostObservable(url, false).map(this::mapStringOrError).ignoreElements();
   }

   @Override
   public Observable<DriveItem> stat(Cid id, String path) {
      HttpUrl url = buildUrl(URL_STAT, encode(id), path).build();
      return makeGetObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, StatWrap.class))
            .map(StatWrap::getStat)
            .map(DriveItem::fromDto);
   }

   @Override
   public Observable<List<DriveItem>> ls(Cid id, String path) {
      HttpUrl url = buildUrl(URL_LS, encode(id), path).build();
      return makeGetObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, StatListWrap.class))
            .flatMapIterable(StatListWrap::getList)
            .map(DriveItem::fromDto)
            .toList()
            .toObservable();
   }

   @Override
   public Completable flush(Cid id, String path) {
      HttpUrl url = buildUrl(URL_FLUSH, encode(id), path).build();
      return makePostObservable(url, false).map(this::mapStringOrError).ignoreElements();
   }

   /**
    * create new request body from the content
    * 
    * @param content the drive content used to create request body
    */
   protected static RequestBody createRequestBody(DriveContent content) {
      if (content instanceof RawInputStreamContent) {
         RawInputStreamContent rawContent = (RawInputStreamContent) content;
         return new RawMultipartRequestContent(rawContent.getContentType(), rawContent.getInputStream());
      } else {
         return new MultipartRequestContent(content, MultipartRequestContent.createBoundary());
      }
   }
}
