/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * CID DTO representation
 */
public class CidDTO {
   @SerializedName("Id")
   private final String id;

   /**
    * @param id string representation
    */
   public CidDTO(String id) {
      this.id = id;
   }

   /**
    * @return the id
    */
   public String getId() {
      return id;
   }
}
