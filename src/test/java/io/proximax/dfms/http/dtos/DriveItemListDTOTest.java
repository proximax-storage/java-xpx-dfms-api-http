/**
 * 
 */
package io.proximax.dfms.http.dtos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * {@link DriveItemListDTO} tests
 */
class DriveItemListDTOTest {

   @Test
   void test() {
      DriveItemDTO item = Mockito.mock(DriveItemDTO.class);
      DriveItemListDTO dto = new DriveItemListDTO(Arrays.asList(item));
      
      assertEquals(Arrays.asList(item), dto.getItems());
   }

}
