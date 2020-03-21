package io.proximax.dfms.privacy;

/**
 * Enumerates the different privacy types
 * 
 * @see io.proximax.dfms.privacy.PrivacyStrategy
 */
public enum PrivacyType {

   /**
    * The type for plain privacy strategy
    * 
    * @see io.proximax.dfms.privacy.strategies.PlainPrivacyStrategy
    */
   PLAIN(1001),
   /**
    * The type for secured with ProximaX keys privacy strategy
    * 
    * @see io.proximax.dfms.privacy.strategies.ProximaXKeysPrivacyStrategy
    */
   PROXIMAX_KEYS(1002),
   /**
    * The type for secured with shamir secret sharing privacy strategy
    * 
    * @see io.proximax.dfms.privacy.strategies.ShamirSecretSharingPrivacyStrategy
    */
   SHAMIR(1003),
   /**
    * The type for secured with password privacy strategy
    * 
    * @see io.proximax.dfms.privacy.strategies.PasswordPrivacyStrategy
    */
   PASSWORD(1004),
   /**
    * The type for custom privacy strategy
    * 
    * @see io.proximax.dfms.privacy.strategies.CustomPrivacyStrategy
    */
   CUSTOM(2001);

   private final int value;

   /**
    * create enum instance with numeric value
    * 
    * @param value the id of the strategy
    */
   private PrivacyType(int value) {
      this.value = value;
   }

   /**
    * Get the int value of the privacy type
    * 
    * @return the privacy type's int value
    */
   public int getValue() {
      return value;
   }
}
