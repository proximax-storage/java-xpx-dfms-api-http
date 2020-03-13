/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.exceptions;

/**
 * Types of errors that are returned by the HTTP API calls
 */
public enum ResponseErrorType {
   /**
    * is a normal error. The command failed for some reason that is not a bug.
    */
   NORMAL(0),
   
   /**
    * client made an invalid request.
    */
   CLIENT(1),
   
   /**
    * there is a bug in the implementation.
    */
   IMPLEMENTATION(2),
   
   /**
    * the operation has been rate-limited.
    */
   RATE_LIITED(3),
   
   /**
    * the client doesn't have permission to perform the requested operation.
    */
   FORBIDDEN(4);
   
   private final int code;

   /**
    * @param code
    */
   private ResponseErrorType(int code) {
      this.code = code;
   }

   /**
    * @return the code
    */
   public int getCode() {
      return code;
   }
   
   /**
    * retrieve instance by the actual code
    * 
    * @param code the code indicating type of error
    * @return the instance
    */
   public static ResponseErrorType getByCode(int code) {
      for (ResponseErrorType err: values()) {
         if (err.getCode() == code) {
            return err;
         }
      }
      throw new DFMSRuntimeException("Unrecognized error code " + code);
   }
}
