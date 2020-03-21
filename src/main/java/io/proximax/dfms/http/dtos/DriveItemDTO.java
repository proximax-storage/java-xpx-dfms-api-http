/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Drive item DTO represents description of drive content
 */
public class DriveItemDTO {
   @SerializedName("Name")
   private final String name;
   @SerializedName("Type")
   private final String type;
   @SerializedName("Size")
   private final long size;
   @SerializedName("Cid")
   private final DriveItemCid cid;
   
   /**
    * @param name name of the drive item
    * @param type type of the drive item (file/dir)
    * @param size size of the file or 0 for directory
    * @param cid representation of CID
    */
   public DriveItemDTO(String name, String type, long size, DriveItemCid cid) {
      this.name = name;
      this.type = type;
      this.size = size;
      this.cid = cid;
   }
   /**
    * @return the name
    */
   public String getName() {
      return name;
   }
   /**
    * @return the type
    */
   public String getType() {
      return type;
   }
   /**
    * @return the size
    */
   public long getSize() {
      return size;
   }
   /**
    * @return the cid
    */
   public String getCid() {
      return cid.getCid();
   }
   
   /**
    * cid is present wrapped in an object with key /
    */
   public static final class DriveItemCid {
      @SerializedName("/")
      private final String cid;

      /**
       * @param cid the actual CID value
       */
      public DriveItemCid(String cid) {
         this.cid = cid;
      }

      /**
       * @return the cid
       */
      public String getCid() {
         return cid;
      }
      
   }
}
