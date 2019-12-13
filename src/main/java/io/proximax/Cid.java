/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax;

import java.util.Objects;

/**
 * TODO add proper description
 */
public class Cid {
   private final String id;

   /**
    * @param id
    */
   public Cid(String id) {
      this.id = id;
   }

   /**
    * @return the id
    */
   public String getId() {
      return id;
   }
   
   public static Cid decode(String str) {
      return new Cid(str);
   }

   @Override
   public String toString() {
      return id;
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Cid other = (Cid) obj;
      return Objects.equals(id, other.id);
   }
   
}
