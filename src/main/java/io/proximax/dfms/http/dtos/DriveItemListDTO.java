/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for response from drive list operation
 */
public class DriveItemListDTO {
   @SerializedName("List")
   private final List<DriveItemDTO> items;

   /**
    * @param items list of items
    */
   public DriveItemListDTO(List<DriveItemDTO> items) {
      this.items = items;
   }

   /**
    * @return the items
    */
   public List<DriveItemDTO> getItems() {
      return items;
   }
}
