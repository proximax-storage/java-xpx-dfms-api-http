/**
 * 
 */
package io.proximax.dfms.model.contract;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

/**
 * Contract options
 */
public class ContractOptions {
   private static final String OPT_REPLICAS = "replicas";
   private static final String OPT_MIN_REPLICATORS = "min-replicators";
   private static final String OPT_SUBS_PRICE = "subscription-price";
   private static final String OPT_NUM_SUBS_PERIODS = "number-subscription-periods";
   private static final String OPT_PCT_APPROVERS = "percent-approvers";

   private final Optional<Integer> replicas;
   private final Optional<Integer> minReplicators;
   private final Optional<Long> subscriptionPrice;
   private final Optional<Integer> subscriptionPeriodsCount;
   private final Optional<Integer> percentApprovers;

   /**
    * @param replicas
    * @param minReplicators
    * @param subscriptionPrice
    * @param subscriptionPeriodsCount
    * @param percentApprovers
    * @param privateKey
    */
   public ContractOptions(Optional<Integer> replicas, Optional<Integer> minReplicators, Optional<Long> subscriptionPrice, Optional<Integer> subscriptionPeriodsCount,
         Optional<Integer> percentApprovers) {
      // validations
      Validate.notNull(replicas);
      Validate.notNull(minReplicators);
      Validate.notNull(subscriptionPrice);
      Validate.notNull(subscriptionPeriodsCount);
      Validate.notNull(percentApprovers);
      // make the assignments
      this.replicas = replicas;
      this.minReplicators = minReplicators;
      this.subscriptionPrice = subscriptionPrice;
      this.subscriptionPeriodsCount = subscriptionPeriodsCount;
      this.percentApprovers = percentApprovers;
   }

   /**
    * get contract options as map of key-value pairs
    * 
    * @return options as key-value pairs
    */
   public Map<String, String> asOptionMap() {
      Map<String, String> options = new HashMap<>();
      addOptionWhenNotNull(options, OPT_REPLICAS, getReplicas());
      addOptionWhenNotNull(options, OPT_MIN_REPLICATORS, getMinReplicators());
      addOptionWhenNotNull(options, OPT_SUBS_PRICE, getSubscriptionPrice());
      addOptionWhenNotNull(options, OPT_NUM_SUBS_PERIODS, getSubscriptionPeriodsCount());
      addOptionWhenNotNull(options, OPT_PCT_APPROVERS, getPercentApprovers());
      return options;
   }
   
   /**
    * return empty contract options and rely on default values for options
    * 
    * @return empty options
    */
   public static ContractOptions empty() {
      return new Builder().build();
   }
   
   /**
    * if value is not null then add it to the options map with specified key
    * @param options options map where the key will be added when appropriate
    * @param key key that will be used in the map
    * @param value value of the option
    */
   protected static void addOptionWhenNotNull(final Map<String, String> options, final String key, final Optional<?> value) {
      if (value.isPresent()) {
         options.put(key, String.valueOf(value.get()));
      }
   }

   /**
    * @return the replicas
    */
   protected Optional<Integer> getReplicas() {
      return replicas;
   }

   /**
    * @return the minReplicators
    */
   protected Optional<Integer> getMinReplicators() {
      return minReplicators;
   }

   /**
    * @return the subscriptionPrice
    */
   protected Optional<Long> getSubscriptionPrice() {
      return subscriptionPrice;
   }

   /**
    * @return the subscriptionPeriodsCount
    */
   protected Optional<Integer> getSubscriptionPeriodsCount() {
      return subscriptionPeriodsCount;
   }

   /**
    * @return the percentApprovers
    */
   protected Optional<Integer> getPercentApprovers() {
      return percentApprovers;
   }

   /**
    * Builder providing convenience and defaults when constructing the Contract composition options
    */
   public static class Builder {

      private Optional<Integer> replicas = Optional.empty();
      private Optional<Integer> minReplicators = Optional.empty();
      private Optional<Long> subscriptionPrice = Optional.empty();
      private Optional<Integer> subscriptionPeriodsCount = Optional.empty();
      private Optional<Integer> percentApprovers = Optional.empty();

      /**
       * build new instance
       * 
       * @return new instance of contract options
       */
      public ContractOptions build() {
         return new ContractOptions(replicas, minReplicators, subscriptionPrice, subscriptionPeriodsCount,
               percentApprovers);
      }

      /**
       * @return the replicas
       */
      public Optional<Integer> getReplicas() {
         return replicas;
      }

      /**
       * @param replicas the replicas to set
       */
      public void replicas(Integer replicas) {
         this.replicas = Optional.of(replicas);
      }

      /**
       * @return the minReplicators
       */
      public Optional<Integer> getMinReplicators() {
         return minReplicators;
      }

      /**
       * @param minReplicators the minReplicators to set
       */
      public void minReplicators(Integer minReplicators) {
         this.minReplicators = Optional.of(minReplicators);
      }

      /**
       * @return the subscriptionPrice
       */
      public Optional<Long> getSubscriptionPrice() {
         return subscriptionPrice;
      }

      /**
       * @param subscriptionPrice the subscriptionPrice to set
       */
      public void subscriptionPrice(Long subscriptionPrice) {
         this.subscriptionPrice = Optional.of(subscriptionPrice);
      }

      /**
       * @return the subscriptionPeriodsCount
       */
      public Optional<Integer> getSubscriptionPeriodsCount() {
         return subscriptionPeriodsCount;
      }

      /**
       * @param subscriptionPeriodsCount the subscriptionPeriodsCount to set
       */
      public void subscriptionPeriodsCount(Integer subscriptionPeriodsCount) {
         this.subscriptionPeriodsCount = Optional.of(subscriptionPeriodsCount);
      }

      /**
       * @return the percentApprovers
       */
      public Optional<Integer> getPercentApprovers() {
         return percentApprovers;
      }

      /**
       * @param percentApprovers the percentApprovers to set
       */
      public void percentApprovers(Integer percentApprovers) {
         this.percentApprovers = Optional.of(percentApprovers);
      }

   }
}
