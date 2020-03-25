/**
 * 
 */
package io.proximax.dfms.http.dtos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link PeerIdDTO} tests
 */
class PeerIdDTOTest {

   @Test
   void test() {
      PeerIdDTO dto = new PeerIdDTO("id");
      assertEquals("id",  dto.getId());
   }

}
