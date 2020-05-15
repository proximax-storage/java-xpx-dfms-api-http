/**
 * 
 */
package io.proximax.dfms.model.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

/**
 * {@link ContractDuration} tests
 */
class ContractDurationTest {

   @Test
   void test() {
      ContractDuration duration = ContractDuration.ofDays(3);
      assertEquals(Duration.ofDays(3), duration.getDuration());
      assertEquals(duration, ContractDuration.of(3, ChronoUnit.DAYS));
      assertEquals(duration, ContractDuration.of(3*24, ChronoUnit.HOURS));
      assertEquals(duration, ContractDuration.of(3*24*3600, ChronoUnit.SECONDS));
      assertEquals(Long.toString(3*24*3600)+"000000000", duration.encode());
   }
   
   @SuppressWarnings("unlikely-arg-type")
   @Test
   void testEquals() {
      ContractDuration a1 = ContractDuration.ofDays(1);
      ContractDuration a2 = ContractDuration.ofDays(1);
      ContractDuration b1 = ContractDuration.ofDays(2);
      assertTrue(a1.equals(a1));
      assertTrue(a1.equals(a2));
      assertFalse(a1.equals(b1));
      assertFalse(a1.equals("different class"));
      assertFalse(a1.equals(null));
   }
   
   @Test
   void testHashCode() {
      ContractDuration a1 = ContractDuration.ofDays(1);
      ContractDuration a2 = ContractDuration.ofDays(1);
      ContractDuration b1 = ContractDuration.ofDays(2);
      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b1.hashCode());
   }
}
