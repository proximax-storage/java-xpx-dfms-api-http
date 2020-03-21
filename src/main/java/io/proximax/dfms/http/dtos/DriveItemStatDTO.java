/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for response from drive stat operation
 */
public class DriveItemStatDTO {
   @SerializedName("Stat")
   private final DriveItemDTO item;

   /**
    * @param item drive item
    */
   public DriveItemStatDTO(DriveItemDTO item) {
      this.item = item;
   }

   /**
    * @return the item
    */
   public DriveItemDTO getItem() {
      return item;
   }

}
