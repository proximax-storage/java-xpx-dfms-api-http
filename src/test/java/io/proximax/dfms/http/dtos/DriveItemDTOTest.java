/**
 * 
 */
package io.proximax.dfms.http.dtos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link DriveItemDTO} tests
 */
class DriveItemDTOTest {

   @Test
   void test() {
      DriveItemDTO dto = new DriveItemDTO("name", "type", 123l, new DriveItemDTO.DriveItemCid("cid"));
      assertEquals("name", dto.getName());
      assertEquals("type", dto.getType());
      assertEquals(123l, dto.getSize());
      assertEquals("cid", dto.getCid());
   }

}
