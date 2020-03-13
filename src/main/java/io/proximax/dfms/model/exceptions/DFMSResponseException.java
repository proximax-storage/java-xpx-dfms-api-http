/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.exceptions;

/**
 * Errors received in response to calls to the HTTP API
 */
public class DFMSResponseException extends DFMSRuntimeException {
   private static final long serialVersionUID = 2997408644250390889L;
   
   private final ResponseErrorType type;
   
   /**
    * @param type type of the error as specified by the code in the response
    * @param message the message from the server
    */
   public DFMSResponseException(ResponseErrorType type, String message) {
      super(message);
      this.type = type;
   }

   /**
    * @return the type
    */
   public ResponseErrorType getType() {
      return type;
   }
   
}
