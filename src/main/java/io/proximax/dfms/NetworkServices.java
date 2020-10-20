/*
 * Copyright 2020 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.util.List;

import io.libp2p.core.PeerId;
import io.libp2p.core.multiformats.Multiaddr;
import io.proximax.dfms.model.network.PeerInfo;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Remote access to drive disk.
 */
public interface NetworkServices {
   
   /**
    * Connect to a node
    * 
    * @param addresses addresses to connect to
    * @return completable allowing blocking wait
    */
   Completable connect(Multiaddr ... addresses);
   
   /**
    * Disconnect from a node
    * 
    * @param addresses addresses to disconnect from
    * @return completable allowing blocking wait
    */
   Completable disconnect(Multiaddr ... addresses);
   
   /**
    * Get all connected peers.
    * 
    * @return observable list of peers
    */
   Observable<List<PeerInfo>> getPeers();
   
   /**
    * Get ID of the node
    * 
    * @return the ID of the node
    */
   Observable<PeerId> getId();
   
   /**
    * Get all node addresses.
    * 
    * @return observable list of addresses of the node
    */
   Observable<List<Multiaddr>> getAddresses();
}
