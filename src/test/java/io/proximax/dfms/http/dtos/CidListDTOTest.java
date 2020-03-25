/**
 * 
 */
package io.proximax.dfms.http.dtos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * {@link CidListDTO} tests
 */
class CidListDTOTest {

   @Test
   void test() {
      CidListDTO dto = new CidListDTO(Arrays.asList("hello", "world"));
      assertEquals(Arrays.asList("hello", "world"), dto.getIds());
   }

}
