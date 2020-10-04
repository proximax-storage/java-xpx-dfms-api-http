/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for deserialization of invites
 * {"drive":"baegaajaiaqjcattw2gtaicwx5qqf5ijypc76jh4fqufgqulxk7ch7myqe7orztbs","owner":"0804122073472a2e9dcea5c2a36eb7f6a34a634010391ec89e883d67360db16f28b9443c","duration":86400,"space":1000000,"payedReplicas":3,"minReplicators":3,"percentApprovers":66,"billingPrice":3000000,"billingPeriod":28800}
 */
public class InviteDTO {
   @SerializedName("drive")
   private final String drive;
   @SerializedName("owner")
   private final String owner;
   /**
    * @param drive
    * @param owner
    */
   public InviteDTO(String drive, String owner) {
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
