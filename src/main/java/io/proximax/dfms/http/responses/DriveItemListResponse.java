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
public class DriveItemListResponse {
   @SerializedName("List")
   private final List<DriveItemResponse> items;

   /**
    * @param items
    */
   public DriveItemListResponse(List<DriveItemResponse> items) {
      this.items = items;
   }

   /**
    * @return the items
    */
   public List<DriveItemResponse> getItems() {
      return items;
   }
}
