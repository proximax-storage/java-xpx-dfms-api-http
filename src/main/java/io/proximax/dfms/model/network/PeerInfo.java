/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.network;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.libp2p.core.PeerId;
import io.libp2p.core.multiformats.Multiaddr;

/**
 * AddrInfo is used to pass around a peer with a set of addresses (and later, keys?).
 */
public class PeerInfo {
   private final PeerId id;
   private final List<Multiaddr> addresses;
   /**
    * @param id the ID of the peer
    * @param addresses addresses of the peer
    */
   public PeerInfo(PeerId id, Multiaddr ... addresses) {
      this.id = id;
      this.addresses = Collections.unmodifiableList(Arrays.asList(addresses));
   }
   /**
    * @return the id
    */
   public PeerId getId() {
      return id;
   }
   /**
    * @return the addresses
    */
   public List<Multiaddr> getAddresses() {
      return addresses;
   }
   
}
