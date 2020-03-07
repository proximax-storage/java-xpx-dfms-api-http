package io.proximax.dfms.model;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * This class specifies the constants used by this SDK
 */
public class Constants {

    /**
     * The upload schema version
     */
    public static final String SCHEMA_VERSION = "1.0";

    /**
     * The content type to indicate it is a path upload
     */
    public static final String PATH_UPLOAD_CONTENT_TYPE = "ipfs/directory";

    /**
     * The reserved content types that cannot be specified as content type when uploading
     */
    public static final List<String> RESERVED_CONTENT_TYPES = asList(PATH_UPLOAD_CONTENT_TYPE);

    private Constants() {
    }
}
