/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.responses;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * TODO add proper description
 */
public class CidListResponse {
   @SerializedName("Ids")
   private final List<String> ids;

   /**
    * @param ids
    */
   public CidListResponse(List<String> ids) {
      this.ids = ids;
   }

   /**
    * @return the ids
    */
   public List<String> getIds() {
      return ids;
   }
}
