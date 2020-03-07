package io.proximax.dfms.model;

import io.proximax.dfms.privacy.strategy.PrivacyStrategy;

/**
 * This model class represents the transaction message
 * @see PrivacyType
 * @see PrivacyStrategy
 */
public final class ProximaxMessagePayloadModel {

    private final int privacyType;
    private final String version;
    private ProximaxDataModel data;

    private ProximaxMessagePayloadModel(int privacyType, String version, ProximaxDataModel data) {
        this.privacyType = privacyType;
        this.version = version;
        this.data = data;
    }

    /**
     * Get the privacy type from privacy strategy used to encrypt data
     * @return the privacy type
     * @see io.proximax.dfms.model.PrivacyType
     */
    public int getPrivacyType() {
        return privacyType;
    }

    /**
     * Get the schema version of the upload
     * @return the schema version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get the data object containing the data hash
     * @return the data object
     */
    public ProximaxDataModel getData() {
        return data;
    }

    /**
     * Construct instance of this model
     * @param privacyType the privacy type from privacy strategy used to encrypt data
     * @param version the schema version of the upload
     * @param data the data object containing the data hash
     * @return instance of this model
     */
    public static ProximaxMessagePayloadModel create(int privacyType, String version, ProximaxDataModel data) {
        return new ProximaxMessagePayloadModel(privacyType, version, data);
    }

}
