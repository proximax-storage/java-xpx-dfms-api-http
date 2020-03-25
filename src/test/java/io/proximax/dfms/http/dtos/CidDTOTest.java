package io.proximax.dfms.http.dtos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link CidDTO} tests
 */
class CidDTOTest {

   @Test
   void test() {
      CidDTO dto = new CidDTO("stringid");
      assertEquals("stringid", dto.getId());
   }

}
