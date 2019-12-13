/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.responses;

import com.google.gson.annotations.SerializedName;

/**
 * TODO add proper description
 */
public class CidResponse {
   @SerializedName("Id")
   private final String id;

   /**
    * @param id
    */
   public CidResponse(String id) {
      this.id = id;
   }

   /**
    * @return the id
    */
   public String getId() {
      return id;
   }
}
