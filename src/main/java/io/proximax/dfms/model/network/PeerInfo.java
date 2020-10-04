/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.network;

import java.util.List;
import java.util.stream.Collectors;

import io.libp2p.core.PeerId;
import io.libp2p.core.multiformats.Multiaddr;
import io.proximax.dfms.http.dtos.PeerInfoDTO;

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
   public PeerInfo(PeerId id, List<Multiaddr> addresses) {
      this.id = id;
      this.addresses = addresses;
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
   
   public static PeerInfo fromDto(PeerInfoDTO dto) {
      List<Multiaddr> addresses = dto.getAddresses().stream().map(Multiaddr::new).collect(Collectors.toList());
      return new PeerInfo(PeerId.fromBase58(dto.getId()), addresses);
   }
   
   public static List<PeerInfo> fromDtos(List<PeerInfoDTO> dtos) {
      return dtos.stream().map(PeerInfo::fromDto).collect(Collectors.toList());

   }
}
