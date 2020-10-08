/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import com.google.gson.annotations.SerializedName;

public class AcceptationDTO {
   @SerializedName("drive")
   private final String drive;
   @SerializedName("owner")
   private final String owner;
   
   /**
    * @param drive
    * @param owner
    */
   public AcceptationDTO(String drive, String owner) {
      this.drive = drive;
      this.owner = owner;
   }
   /**
    * @return the drive
    */
   public String getDrive() {
      return drive;
   }
   /**
    * @return the owner
    */
   public String getOwner() {
      return owner;
   }

}
