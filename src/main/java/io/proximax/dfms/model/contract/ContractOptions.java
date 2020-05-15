/**
 * 
 */
package io.proximax.dfms.model.contract;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import io.proximax.core.crypto.PrivateKey;

/**
 * Contract options
 */
public class ContractOptions {
   private static final String OPT_REPLICAS = "replicas";
   private static final String OPT_MIN_REPLICATORS = "min-replicators";
   private static final String OPT_SUBS_PRICE = "subscription-price";
   private static final String OPT_NUM_SUBS_PERIODS = "number-subscription-periods";
   private static final String OPT_PCT_APPROVERS = "percent-approvers";
   private static final String OPT_PRIVATE_KEY = "private-key";

   private final int replicas;
   private final int minReplicators;
   private final long subscriptionPrice;
   private final int subscriptionPeriodsCount;
   private final int percentApprovers;
   private final PrivateKey privateKey;

   /**
    * @param replicas
    * @param minReplicators
    * @param subscriptionPrice
    * @param subscriptionPeriodsCount
    * @param percentApprovers
    * @param privateKey
    */
   public ContractOptions(int replicas, int minReplicators, long subscriptionPrice, int subscriptionPeriodsCount,
         int percentApprovers, PrivateKey privateKey) {
      // validations
      Validate.isTrue(replicas > 0);
      Validate.isTrue(minReplicators > 0);
      Validate.isTrue(subscriptionPrice > 0);
      Validate.isTrue(subscriptionPeriodsCount > 0);
      Validate.inclusiveBetween(0, 100, percentApprovers);
      Validate.notNull(privateKey);
      // make the assignments
      this.replicas = replicas;
      this.minReplicators = minReplicators;
      this.subscriptionPrice = subscriptionPrice;
      this.subscriptionPeriodsCount = subscriptionPeriodsCount;
      this.percentApprovers = percentApprovers;
      this.privateKey = privateKey;
   }

   /**
    * get contract options as map of key-value pairs
    * 
    * @return options as key-value pairs
    */
   public Map<String, String> asOptionMap() {
      Map<String, String> options = new HashMap<>();
      options.put(OPT_REPLICAS, String.valueOf(getReplicas()));
      options.put(OPT_MIN_REPLICATORS, String.valueOf(getMinReplicators()));
      options.put(OPT_SUBS_PRICE, String.valueOf(getSubscriptionPrice()));
      options.put(OPT_NUM_SUBS_PERIODS, String.valueOf(getSubscriptionPeriodsCount()));
      options.put(OPT_PCT_APPROVERS, String.valueOf(getPercentApprovers()));
      options.put(OPT_PRIVATE_KEY, String.valueOf(getPrivateKey()));
      return options;
   }

   /**
    * @return the replicas
    */
   public int getReplicas() {
      return replicas;
   }

   /**
    * @return the minReplicators
    */
   public int getMinReplicators() {
      return minReplicators;
   }

   /**
    * @return the subscriptionPrice
    */
   public long getSubscriptionPrice() {
      return subscriptionPrice;
   }

   /**
    * @return the subscriptionPeriodsCount
    */
   public int getSubscriptionPeriodsCount() {
      return subscriptionPeriodsCount;
   }

   /**
    * @return the percentApprovers
    */
   public int getPercentApprovers() {
      return percentApprovers;
   }

   /**
    * @return the privateKey
    */
   public PrivateKey getPrivateKey() {
      return privateKey;
   }

   /**
    * Builder providing convenience and defaults when constructing the Contract composition options
    */
   public static class Builder {
      private static final int DEF_REPLICAS = 3;
      private static final int DEF_MIN_REPLICATORS = 3;
      private static final int DEF_NUM_SUBS_PERIODS = 3;
      private static final int DEF_PCT_APPROVERS = 66;

      private int replicas = DEF_REPLICAS;
      private int minReplicators = DEF_MIN_REPLICATORS;
      private Long subscriptionPrice;
      private int subscriptionPeriodsCount = DEF_NUM_SUBS_PERIODS;
      private int percentApprovers = DEF_PCT_APPROVERS;
      private PrivateKey privateKey;

      /**
       * build new instance
       * 
       * @return new instance of contract options
       */
      public ContractOptions build() {
         return new ContractOptions(replicas, minReplicators, subscriptionPrice, subscriptionPeriodsCount,
               percentApprovers, privateKey);
      }

      /**
       * @return the replicas
       */
      public int getReplicas() {
         return replicas;
      }

      /**
       * @param replicas the replicas to set
       */
      public Builder replicas(int replicas) {
         this.replicas = replicas;
         return this;
      }

      /**
       * @return the minReplicators
       */
      public int getMinReplicators() {
         return minReplicators;
      }

      /**
       * @param minReplicators the minReplicators to set
       */
      public Builder minReplicators(int minReplicators) {
         this.minReplicators = minReplicators;
         return this;
      }

      /**
       * @return the subscriptionPrice
       */
      public long getSubscriptionPrice() {
         return subscriptionPrice;
      }

      /**
       * @param subscriptionPrice the subscriptionPrice to set
       */
      public Builder subscriptionPrice(long subscriptionPrice) {
         this.subscriptionPrice = subscriptionPrice;
         return this;
      }

      /**
       * compute price based on required space and number of replicas. Note that this uses number of replicas which are
       * currently set to the builder
       * 
       * @param space requested drive space
       */
      public Builder subscriptionPriceBySpace(BigInteger space) {
         this.subscriptionPrice = space.multiply(BigInteger.valueOf(getReplicas())).longValueExact();
         return this;
      }

      /**
       * @return the subscriptionPeriodsCount
       */
      public int getSubscriptionPeriodsCount() {
         return subscriptionPeriodsCount;
      }

      /**
       * @param subscriptionPeriodsCount the subscriptionPeriodsCount to set
       */
      public Builder subscriptionPeriodsCount(int subscriptionPeriodsCount) {
         this.subscriptionPeriodsCount = subscriptionPeriodsCount;
         return this;
      }

      /**
       * @return the percentApprovers
       */
      public int getPercentApprovers() {
         return percentApprovers;
      }

      /**
       * @param percentApprovers the percentApprovers to set
       */
      public Builder percentApprovers(int percentApprovers) {
         this.percentApprovers = percentApprovers;
         return this;
      }

      /**
       * @return the privateKey
       */
      public PrivateKey getPrivateKey() {
         return privateKey;
      }

      /**
       * @param privateKey the privateKey to set
       */
      public Builder privateKey(PrivateKey privateKey) {
         this.privateKey = privateKey;
         return this;
      }

   }
}
