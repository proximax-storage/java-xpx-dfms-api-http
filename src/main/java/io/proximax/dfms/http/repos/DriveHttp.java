/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import io.proximax.cid.Cid;
import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.http.MultipartRequestContent;
import io.proximax.dfms.http.dtos.CidDTO;
import io.proximax.dfms.http.dtos.DriveItemListDTO;
import io.proximax.dfms.http.dtos.DriveItemStatDTO;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.DriveItem;
import io.proximax.dfms.model.drive.content.InputStreamContent;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Drive repository implementation using HTTP protocol
 */
public class DriveHttp extends HttpRepository<StorageApi> implements DriveRepository {

   private static final String URL_ADD = "drive/add";
   private static final String URL_GET = "drive/get";
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
    */
   public DriveHttp(StorageApi api, String apiPath, OkHttpClient client) {
      super(api, Optional.of(apiPath), client);
   }

   @Override
   public Observable<Cid> add(Cid id, String path, DriveContent content) throws IOException {
      HttpUrl url = buildUrl(URL_ADD, id.toString(), path).build();
      Request request = new Request.Builder().url(url).post(new MultipartRequestContent(content)).build();
      // make the request
      return makeRequest(request).map(this::mapStringOrError).map(str -> getGson().fromJson(str, CidDTO.class))
            .map(CidDTO::getId).map(Cid::decode);
   }

   @Override
   public Observable<DriveContent> get(Cid id, String path) {
      HttpUrl url = buildUrl(URL_GET, id.toString(), path).build();
      Request request = new Request.Builder().url(url).build();
      // caller is responsible to call close on the input stream
      return makeRequest(request).map(this::mapRespBodyOrError)
            .map(resp -> new InputStreamContent(Optional.empty(), resp.byteStream()));
   }

   @Override
   public Completable remove(Cid id, String path) {
      HttpUrl url = buildUrl(URL_REMOVE, id.toString(), path).build();
      Request request = new Request.Builder().url(url).build();
      Call call = getClient().newCall(request);
      return Completable.fromAction(call::execute);
   }

   @Override
   public Completable move(Cid id, String sourcePath, String destinationPath) {
      HttpUrl url = buildUrl(URL_MOVE, id.toString(), sourcePath, destinationPath).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      Call call = getClient().newCall(request);
      return Completable.fromAction(call::execute);
   }

   @Override
   public Completable copy(Cid id, String sourcePath, String destinationPath) {
      HttpUrl url = buildUrl(URL_COPY, id.toString(), sourcePath, destinationPath).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      Call call = getClient().newCall(request);
      return Completable.fromAction(call::execute);
   }

   @Override
   public Completable makeDir(Cid id, String path) {
      HttpUrl url = buildUrl(URL_MKDIR, id.toString(), path).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      Call call = getClient().newCall(request);
      return Completable.fromAction(call::execute);

   }

   @Override
   public Observable<DriveItem> stat(Cid id, String path) {
      HttpUrl url = buildUrl(URL_STAT, id.toString(), path).build();
      Request request = new Request.Builder().url(url).build();
      return makeRequest(request)
         .map(this::mapStringOrError)
         .map(str -> getGson().fromJson(str, DriveItemStatDTO.class))
         .map(DriveItemStatDTO::getItem)
         .map(DriveItem::fromDto);
   }

   @Override
   public Observable<List<DriveItem>> ls(Cid id, String path) {
      HttpUrl url = buildUrl(URL_LS, id.toString(), path).build();
      Request request = new Request.Builder().url(url).build();
      return makeRequest(request)
         .map(this::mapStringOrError)
         .map(str -> getGson().fromJson(str, DriveItemListDTO.class))
         .flatMapIterable(DriveItemListDTO::getItems)
         .map(DriveItem::fromDto)
         .toList().toObservable();
   }

   @Override
   public Completable flush(Cid id, String path) {
      HttpUrl url = buildUrl(URL_FLUSH, id.toString(), path).build();
      Request request = new Request.Builder().url(url).build();
      Call call = getClient().newCall(request);
      return Completable.fromAction(call::execute);
   
   }
}
