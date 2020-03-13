/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.drive;

import java.util.Objects;

import io.proximax.cid.Cid;
import io.proximax.dfms.http.responses.DriveItemResponse;

/**
 * TODO add proper description
 */
public class DriveItem {
   private final String name;
   private final DriveItemType type;
   private final long size;
   private final Cid cid;

   /**
    * @param name
    * @param type
    * @param size
    * @param cid
    */
   public DriveItem(String name, DriveItemType type, long size, Cid cid) {
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
   public DriveItemType getType() {
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
   public Cid getCid() {
      return cid;
   }

   @Override
   public int hashCode() {
      return Objects.hash(cid);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      DriveItem other = (DriveItem) obj;
      return Objects.equals(cid, other.cid);
   }

   @Override
   public String toString() {
      return "DriveItem [name=" + name + ", type=" + type + ", size=" + size + ", cid=" + cid + "]";
   }

   /**
    * decode DTO to create instance of drive item
    * 
    * @param dto response representation
    * @return instance of drive item
    */
   public static DriveItem fromDto(DriveItemResponse dto) {
      return new DriveItem(dto.getName(), DriveItemType.getByCode(dto.getType()), dto.getSize(),
            Cid.decode(dto.getCid()));
   }
}
