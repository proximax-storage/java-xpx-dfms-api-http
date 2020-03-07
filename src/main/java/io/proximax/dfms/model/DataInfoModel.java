package io.proximax.dfms.model;

import java.util.Collections;
import java.util.Map;

/**
 * This model class defines the details of a data
 */
public abstract class DataInfoModel {

    private final String description;
    private final Map<String, String> metadata;
    private final String name;
    private final String contentType;

    /**
     * Construct instance of this model
     * @param description a description of the data
     * @param name the name for the data
     * @param contentType the content type of the data
     * @param metadata the string-to-string keypair metadata of the data
     */
    public DataInfoModel(String description, String name, String contentType, Map<String, String> metadata) {
        this.description = description;
        this.metadata = metadata == null ? null : Collections.unmodifiableMap(metadata);
        this.name = name;
        this.contentType = contentType;
    }

    /**
     * Get the description of the data
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the string-to-string keypair metadata of the data
     * @return the metadata
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Get the name for the data
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the content type of the data
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }
}
