/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.util.List;

import io.libp2p.core.PeerId;
import io.libp2p.core.multiformats.Multiaddr;
import io.proximax.dfms.network.PeerInfo;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * TODO add proper description
 */
public interface NetworkRepository {
   Completable connect(Multiaddr ... addresses);
   
   Completable disconnect(Multiaddr ... addresses);
   
   Observable<List<PeerInfo>> getPeers();
   
   Observable<PeerId> getId();
   
   Observable<List<Multiaddr>> getAddresses();
}
