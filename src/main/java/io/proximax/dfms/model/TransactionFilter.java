package io.proximax.dfms.model;

/**
 * Enumerates the different search transaction filter
 * @see io.proximax.dfms.privacy.strategy.PrivacyStrategy
 */
public enum TransactionFilter {

    /**
     * The type for searching all account transactions (incoming and outgoing)
     */
    ALL,
    /**
     * The type for searching incoming account transactions
     */
    OUTGOING,
    /**
     * The type for searching outgoing account transactions
     */
    INCOMING;
}
