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
public class DriveItemStatResponse {
   @SerializedName("Stat")
   private final DriveItemResponse item;

   /**
    * @param item
    */
   public DriveItemStatResponse(DriveItemResponse item) {
      this.item = item;
   }

   /**
    * @return the item
    */
   public DriveItemResponse getItem() {
      return item;
   }

}
