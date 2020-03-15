/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.http.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for deserialization of error messages
 */
public class ErrorDTO {
   @SerializedName("Message")
   private final String message;

   @SerializedName("Code")
   private final int code;

   @SerializedName("Type")
   private final String type;

   /**
    * @param message
    * @param code
    * @param type
    */
   public ErrorDTO(String message, int code, String type) {
      this.message = message;
      this.code = code;
      this.type = type;
   }

   /**
    * @return the message
    */
   public String getMessage() {
      return message;
   }

   /**
    * @return the code
    */
   public int getCode() {
      return code;
   }

   /**
    * @return the type
    */
   public String getType() {
      return type;
   }
}
