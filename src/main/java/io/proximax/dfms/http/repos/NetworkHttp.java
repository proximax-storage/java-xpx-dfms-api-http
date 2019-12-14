/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.reflect.TypeToken;

import io.libp2p.core.PeerId;
import io.libp2p.core.multiformats.Multiaddr;
import io.proximax.dfms.NetworkRepository;
import io.proximax.dfms.StorageApi;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.network.PeerInfo;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * TODO add proper description
 */
public class NetworkHttp extends HttpRepository<StorageApi> implements NetworkRepository {

   private static final String URL_CONNECT = "net/connect";
   private static final String URL_DISCONNECT = "net/disconnect";
   private static final String URL_PEERS = "net/peers";
   private static final String URL_ID = "net/id";
   private static final String URL_ADDRS = "net/addrs";

   private static final Type PEER_INFO_LIST_TYPE = new TypeToken<List<PeerInfo>>() {
   }.getType();
   private static final Type ADDRS_LIST_TYPE = new TypeToken<List<Multiaddr>>() {
   }.getType();

   /**
    * @param api
    * @param apiPath
    */
   public NetworkHttp(StorageApi api, String apiPath, OkHttpClient client) {
      super(api, Optional.of(apiPath), client);
   }

   @Override
   public Completable connect(Multiaddr... addresses) {
      String[] args = Stream.of(addresses).map(Multiaddr::toString).collect(Collectors.toList())
            .toArray(new String[addresses.length]);
      HttpUrl url = buildUrl(URL_CONNECT, args).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      Call call = getClient().newCall(request);
      return Completable.fromAction(call::execute);
   }

   @Override
   public Completable disconnect(Multiaddr... addresses) {
      String[] args = Stream.of(addresses).map(Multiaddr::toString).collect(Collectors.toList())
            .toArray(new String[addresses.length]);
      HttpUrl url = buildUrl(URL_DISCONNECT, args).build();
      RequestBody body = RequestBody.create(null, new byte[] {});
      Request request = new Request.Builder().url(url).post(body).build();
      Call call = getClient().newCall(request);
      return Completable.fromAction(call::execute);
   }

   @Override
   public Observable<List<PeerInfo>> getPeers() {
      HttpUrl url = buildUrl(URL_PEERS).build();
      Request request = new Request.Builder().url(url).build();
      // caller is responsible to call close on the input stream
      return makeRequest(request).map(this::mapStringOrError).map(this::toPeerInfoList);
   }

   @Override
   public Observable<PeerId> getId() {
      HttpUrl url = buildUrl(URL_ID).build();
      Request request = new Request.Builder().url(url).build();
      // caller is responsible to call close on the input stream
      return makeRequest(request).map(this::mapStringOrError).map(str -> getGson().fromJson(str, PeerId.class));
   }

   @Override
   public Observable<List<Multiaddr>> getAddresses() {
      HttpUrl url = buildUrl(URL_ADDRS).build();
      Request request = new Request.Builder().url(url).build();
      // caller is responsible to call close on the input stream
      return makeRequest(request).map(this::mapStringOrError).map(this::toAddrsList);
   }

   private List<PeerInfo> toPeerInfoList(String json) {
      return getGson().fromJson(json, PEER_INFO_LIST_TYPE);
   }

   private List<Multiaddr> toAddrsList(String json) {
      return getGson().fromJson(json, ADDRS_LIST_TYPE);
   }

}
