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
 * {@link DriveContractDuration} tests
 */
class ContractDurationTest {

   @Test
   void test() {
      DriveContractDuration duration = DriveContractDuration.ofDays(3);
      assertEquals(Duration.ofDays(3), duration.getDuration());
      assertEquals(duration, DriveContractDuration.of(3, ChronoUnit.DAYS));
      assertEquals(duration, DriveContractDuration.of(3*24, ChronoUnit.HOURS));
      assertEquals(duration, DriveContractDuration.of(3*24*3600, ChronoUnit.SECONDS));
      assertEquals(Long.toString(3*24*3600)+"s", duration.encode());
   }
   
   @SuppressWarnings("unlikely-arg-type")
   @Test
   void testEquals() {
      DriveContractDuration a1 = DriveContractDuration.ofDays(1);
      DriveContractDuration a2 = DriveContractDuration.ofDays(1);
      DriveContractDuration b1 = DriveContractDuration.ofDays(2);
      assertTrue(a1.equals(a1));
      assertTrue(a1.equals(a2));
      assertFalse(a1.equals(b1));
      assertFalse(a1.equals("different class"));
      assertFalse(a1.equals(null));
   }
   
   @Test
   void testHashCode() {
      DriveContractDuration a1 = DriveContractDuration.ofDays(1);
      DriveContractDuration a2 = DriveContractDuration.ofDays(1);
      DriveContractDuration b1 = DriveContractDuration.ofDays(2);
      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b1.hashCode());
   }
}
