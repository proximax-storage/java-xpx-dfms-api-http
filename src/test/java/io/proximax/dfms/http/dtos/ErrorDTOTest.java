/**
 * 
 */
package io.proximax.dfms.http.dtos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link ErrorDTO} tests
 */
class ErrorDTOTest {

   @Test
   void testConstructor() {
      ErrorDTO dto = new ErrorDTO("hi", 5, "yeah");
      assertEquals("hi", dto.getMessage());
      assertEquals(5, dto.getCode());
      assertEquals("yeah", dto.getType());
   }

}
