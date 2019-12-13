/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.responses;

import com.google.gson.annotations.SerializedName;

import io.proximax.dfms.gen.model.ContractDTO;

/**
 * TODO add proper description
 */
public class ContractResponse {
   @SerializedName("Contract")
   private final ContractDTO contract;

   /**
    * @param contract
    */
   public ContractResponse(ContractDTO contract) {
      this.contract = contract;
   }

   /**
    * @return the contract
    */
   public ContractDTO getContract() {
      return contract;
   }
}
