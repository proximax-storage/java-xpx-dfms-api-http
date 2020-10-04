/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for response from net/peers request
 */
public class PeerInfoListDTO {
   @SerializedName("Peers")
   private final List<PeerInfoDTO> peers;

   /**
    * @param peers
    */
   public PeerInfoListDTO(List<PeerInfoDTO> peers) {
      this.peers = peers;
   }

   /**
    * @return the peers
    */
   public List<PeerInfoDTO> getPeers() {
      return peers;
   }

}
