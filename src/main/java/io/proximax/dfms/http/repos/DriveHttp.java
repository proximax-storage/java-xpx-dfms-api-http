/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.ipfs.cid.Cid;
import io.proximax.dfms.DriveRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.drive.DriveContent;
import io.proximax.dfms.http.HttpRepository;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Drive repository implementation using HTTP protocol
 */
public class DriveHttp extends HttpRepository<StorageApi> implements DriveRepository {

   private static final String URL_REMOVE = "drive/remove";
   private static final String URL_MOVE = "drive/move";
   private static final String URL_COPY = "drive/copy";

   /**
    * create new instance
    * 
    * @param api the storage API
    * @param apiPath the path to the API on the node
    */
   public DriveHttp(StorageApi api, String apiPath) {
      super(api, Optional.of(apiPath));
   }

   @Override
   public Observable<Cid> add(Cid id, String path, DriveContent content) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<DriveContent> get(Cid id, String path) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Completable remove(Cid id, String path) {
      String requestUrl = getUrl(URL_REMOVE, Arrays.asList(id.toString(), path));
      return getClient().post(requestUrl, null).map(HttpRepository::mapStringOrError).ignoreElements();
   }

   @Override
   public Completable move(Cid id, String sourcePath, String destinationPath) {
      String requestUrl = getUrl(URL_MOVE, Arrays.asList(id.toString(), sourcePath, destinationPath));
      return getClient().post(requestUrl, null).map(HttpRepository::mapStringOrError).ignoreElements();
   }

   @Override
   public Completable copy(Cid id, String sourcePath, String destinationPath) {
      String requestUrl = getUrl(URL_COPY, Arrays.asList(id.toString(), sourcePath, destinationPath));
      return getClient().post(requestUrl, null).map(HttpRepository::mapStringOrError).ignoreElements();
   }

   @Override
   public Completable makeDir(Cid id, String path) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<Path> stat(Cid id, String path) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Observable<List<Path>> ls(Cid id, String path) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Completable flush(Cid id, String path) {
      // TODO Auto-generated method stub
      return null;
   }

   private static String getUrl(String base, List<String> args) {
      // concatenate arguments
      String urlArgs = args.stream().map(arg -> "arg=" + encode(arg)).collect(Collectors.joining("&"));
      // build the url
      StringBuilder url = new StringBuilder(base);
      url.append("?");
      url.append(urlArgs);
      return url.toString();
   }

   private static String encode(String string) {
      try {
         return URLEncoder.encode(string, "UTF-8");
      } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("failed to encode URL string", e);
      }
   }
}
