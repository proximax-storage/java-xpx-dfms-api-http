/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.drive;

/**
 * TODO add proper description
 */
public enum DriveItemType {
   DIRECTORY("dir"), FILE("file");
   
   private final String code;

   /**
    * @param code
    */
   private DriveItemType(String code) {
      this.code = code;
   }

   /**
    * @return the code
    */
   public String getCode() {
      return code;
   }
   
   public static DriveItemType getByCode(String code) {
      for (DriveItemType tp: values()) {
         if (tp.getCode().equals(code)) {
            return tp;
         }
      }
      throw new IllegalArgumentException("Unknown code " + code);
   }
}
