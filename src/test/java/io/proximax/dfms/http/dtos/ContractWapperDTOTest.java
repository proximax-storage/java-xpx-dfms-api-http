/**
 * 
 */
package io.proximax.dfms.http.dtos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.proximax.dfms.gen.model.ContractDTO;

/**
 * {@link ContractWapperDTO} tests
 */
class ContractWapperDTOTest {

   @Test
   void test() {
      ContractDTO contract = Mockito.mock(ContractDTO.class);
      ContractWapperDTO dto = new ContractWapperDTO(contract);
      assertEquals(contract, dto.getContract());
   }

}
