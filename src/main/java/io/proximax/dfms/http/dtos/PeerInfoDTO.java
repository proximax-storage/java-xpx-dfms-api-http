/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for response from net/addrs request
 */
public class PeerInfoDTO {
   @SerializedName("ID")
   private final String id;

   @SerializedName("Addrs")
   private final List<String> addresses;

   /**
    * @param id
    * @param addresses
    */
   public PeerInfoDTO(String id, List<String> addresses) {
      this.id = id;
      this.addresses = addresses;
   }

   /**
    * @return the id
    */
   public String getId() {
      return id;
   }

   /**
    * @return the addresses
    */
   public List<String> getAddresses() {
      return addresses;
   }

}
