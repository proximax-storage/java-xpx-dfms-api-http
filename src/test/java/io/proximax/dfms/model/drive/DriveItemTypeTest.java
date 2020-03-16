package io.proximax.dfms.model.drive;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DriveItemTypeTest {

   @Test
   void iterateAllEntries() {
      for (DriveItemType tp: DriveItemType.values()) {
         assertEquals(tp, DriveItemType.getByCode(tp.getCode()));
      }
   }
   
   @Test
   void badCode() {
      assertThrows(IllegalArgumentException.class, () -> DriveItemType.getByCode("definitely not valid code"));
   }
}
