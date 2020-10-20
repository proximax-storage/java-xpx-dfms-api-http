/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.repos;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.libp2p.core.PeerId;
import io.libp2p.core.multiformats.Multiaddr;
import io.proximax.dfms.NetworkServices;
import io.proximax.dfms.ServiceBase;
import io.proximax.dfms.gen.model.AddrListWrap;
import io.proximax.dfms.gen.model.PeerIdWrap;
import io.proximax.dfms.gen.model.PeerListWrap;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.model.network.PeerInfo;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * TODO add proper description
 */
public class NetworkHttp extends HttpRepository<ServiceBase> implements NetworkServices {

   private static final String URL_CONNECT = "net/connect";
   private static final String URL_DISCONNECT = "net/disconnect";
   private static final String URL_PEERS = "net/peers";
   private static final String URL_ID = "net/id";
   private static final String URL_ADDRS = "net/addrs";

   /**
    * @param api the storage API
    * @param apiPath the path to the API on the node
    * @param client the HTTP client to be used to execute requests
    */
   public NetworkHttp(ServiceBase api, String apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      super(api, Optional.of(apiPath), client, longPollingClient);
   }

   @Override
   public Completable connect(Multiaddr... addresses) {
      String[] args = Stream.of(addresses).map(Multiaddr::toString).collect(Collectors.toList())
            .toArray(new String[addresses.length]);
      HttpUrl url = buildUrl(URL_CONNECT, args).build();
      return makePostCompletable(url);
   }

   @Override
   public Completable disconnect(Multiaddr... addresses) {
      String[] args = Stream.of(addresses).map(Multiaddr::toString).collect(Collectors.toList())
            .toArray(new String[addresses.length]);
      HttpUrl url = buildUrl(URL_DISCONNECT, args).build();
      return makePostCompletable(url);
   }

   @Override
   public Observable<List<PeerInfo>> getPeers() {
      HttpUrl url = buildUrl(URL_PEERS).build();
      Request request = new Request.Builder().url(url).build();
      // caller is responsible to call close on the input stream
      return makeRequest(request, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, PeerListWrap.class))
            .map(PeerListWrap::getPeers)
            .map(PeerInfo::fromDtos);
   }

   @Override
   public Observable<PeerId> getId() {
      HttpUrl url = buildUrl(URL_ID).build();
      Request request = new Request.Builder().url(url).build();
      // caller is responsible to call close on the input stream
      return makeRequest(request, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, PeerIdWrap.class))
            .map(PeerIdWrap::getID)
            .map(PeerId::fromBase58);
   }

   @Override
   public Observable<List<Multiaddr>> getAddresses() {
      HttpUrl url = buildUrl(URL_ADDRS).build();
      Request request = new Request.Builder().url(url).build();
      // caller is responsible to call close on the input stream
      return makeRequest(request, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, AddrListWrap.class))
            .map(AddrListWrap::getAddrs)
            .flatMapIterable(list -> list)
            .map(Multiaddr::new)
            .toList().toObservable();
   }

}
