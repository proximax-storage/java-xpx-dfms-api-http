/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.drive;

import org.apache.commons.lang3.Validate;

import io.proximax.cid.Cid;
import io.proximax.dfms.http.dtos.DriveItemDTO;

/**
 * representation of information about an item on the drive
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
      // validations
      Validate.notNull(name, "name is mandatory");
      Validate.notNull(type, "type is mandatory");
      Validate.notNull(cid, "cid is mandatory");
      // assignments
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
      final int prime = 31;
      int result = 1;
      result = prime * result + ((cid == null) ? 0 : cid.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + (int) (size ^ (size >>> 32));
      result = prime * result + ((type == null) ? 0 : type.hashCode());
      return result;
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
      if (cid == null) {
         if (other.cid != null)
            return false;
      } else if (!cid.equals(other.cid))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      if (size != other.size)
         return false;
      if (type != other.type)
         return false;
      return true;
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
   public static DriveItem fromDto(DriveItemDTO dto) {
      return new DriveItem(dto.getName(), DriveItemType.getByCode(dto.getType()), dto.getSize(),
            Cid.decode(dto.getCid()));
   }
}
