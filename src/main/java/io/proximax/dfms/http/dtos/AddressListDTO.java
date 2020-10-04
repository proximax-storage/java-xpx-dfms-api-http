/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.annotations.SerializedName;

import io.libp2p.core.multiformats.Multiaddr;

/**
 * DTO for response from net/addrs request
 */
public class AddressListDTO {
   @SerializedName("Addrs")
   private final List<String> addresses;

   /**
    * @param addresses
    */
   public AddressListDTO(List<String> addresses) {
      this.addresses = addresses;
   }

   /**
    * @return the addresses
    */
   public List<String> getAddresses() {
      return addresses;
   }
   
   /**
    * @return the addresses as multi address instances
    */
   public List<Multiaddr> getMultiAddresses() {
      return addresses.stream().map(Multiaddr::new).collect(Collectors.toList());
   }
}
