/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for list of CID objects
 */
public class ResultListDTO {
   @SerializedName("Results")
   private final List<String> ids;

   /**
    * @param ids list of string IDs
    */
   public ResultListDTO(List<String> ids) {
      this.ids = ids;
   }

   /**
    * @return the ids
    */
   public List<String> getIds() {
      return ids;
   }
}
