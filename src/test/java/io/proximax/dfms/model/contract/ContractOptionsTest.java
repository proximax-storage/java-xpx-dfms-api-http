/**
 * 
 */
package io.proximax.dfms.model.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * @author tono
 *
 */
class ContractOptionsTest {

   @Test
   void testAddOptionWhenNotNull() {
      Map<String, String> opts = new HashMap<>();
      DriveContractOptions.addOptionWhenNotNull(opts, "test1", Optional.of("val1"));
      assertEquals("val1", opts.get("test1"));
      DriveContractOptions.addOptionWhenNotNull(opts, "test2", Optional.of(1));
      assertEquals("1", opts.get("test2"));
      DriveContractOptions.addOptionWhenNotNull(opts, "test3", Optional.empty());
      assertFalse(opts.containsKey("test3"));
   }

}
