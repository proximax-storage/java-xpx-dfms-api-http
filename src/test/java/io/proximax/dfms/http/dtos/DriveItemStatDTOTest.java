/**
 * 
 */
package io.proximax.dfms.http.dtos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * {@link DriveItemStatDTO} tests
 */
class DriveItemStatDTOTest {

   @Test
   void test() {
      DriveItemDTO item = Mockito.mock(DriveItemDTO.class);
      DriveItemStatDTO dto = new DriveItemStatDTO(item);
      
      assertEquals(item, dto.getItem());
   }

}
