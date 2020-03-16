package io.proximax.dfms.model.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ResponseErrorTypeTest {

   @Test
   void iterateAllEntries() {
      for (ResponseErrorType tp: ResponseErrorType.values()) {
         assertEquals(tp, ResponseErrorType.getByCode(tp.getCode()));
      }
   }
   
   @Test
   void badCode() {
      assertThrows(DFMSRuntimeException.class, () -> ResponseErrorType.getByCode(876876));
   }
}
