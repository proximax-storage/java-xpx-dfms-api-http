/**
 * 
 */
package io.proximax.dfms.model.contract;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

/**
 * DriveContract duration definition
 */
public class DriveContractDuration {

   private final Duration duration;
   
   /**
    * create new instance
    */
   private DriveContractDuration(Duration duration) {
      Validate.notNull(duration);
      this.duration = duration;
   }


   /**
    * @return the duration
    */
   public Duration getDuration() {
      return duration;
   }

   /**
    * encode the duration to URL parameter. Duration is represented as number of seconds
    * 
    * @return string representing number of seconds of the duration with "s" as suffix to indicate unit
    */
   public String encode() {
      return Long.toString(duration.toMillis()/1000) + "s";
   }
   
   /**
    * create new instance based on number of days
    * 
    * @param days number of days of the contract duration
    * @return the contract duration
    */
   public static DriveContractDuration ofDays(int days) {
      return new DriveContractDuration(Duration.ofDays(days));
   }

   /**
    * create new instance based on the amount of specified unit
    * 
    * @param amount number of units
    * @param unit unit of time (see {@link ChronoUnit}
    * @return the contract duration
    */
   public static DriveContractDuration of(long amount, TemporalUnit unit) {
      return new DriveContractDuration(Duration.of(amount, unit));
   }


   @Override
   public int hashCode() {
      return Objects.hash(duration);
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      DriveContractDuration other = (DriveContractDuration) obj;
      return Objects.equals(duration, other.duration);
   }


}
