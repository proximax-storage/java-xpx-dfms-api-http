/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.exceptions;

/**
 * TODO add proper description
 */
public class DFMSRuntimeException extends RuntimeException {
   private static final long serialVersionUID = -2388261742030070521L;

   /**
    * 
    */
   public DFMSRuntimeException() {
   }

   /**
    * @param message
    * @param cause
    */
   public DFMSRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * @param message
    */
   public DFMSRuntimeException(String message) {
      super(message);
   }

}
