/*
 * DFMS API
 * DFMS node HTTP API. [Reference implementation in GO](https://github.com/proximax-storage/go-xpx-dfms-api-http) [API definition](https://github.com/proximax-storage/go-xpx-dfms-api)  API does not tries to follow idiomatic REST or other API patterns for reasons.  
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package io.proximax.dfms.gen.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * Peer
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-10-25T18:34:19.638+01:00[Europe/Prague]")
public class Peer {
  public static final String SERIALIZED_NAME_ADDRS = "Addrs";
  @SerializedName(SERIALIZED_NAME_ADDRS)
  private AddrList addrs = new AddrList();

  public static final String SERIALIZED_NAME_I_D = "ID";
  @SerializedName(SERIALIZED_NAME_I_D)
  private String ID;


  public Peer addrs(AddrList addrs) {
    
    this.addrs = addrs;
    return this;
  }

   /**
   * Get addrs
   * @return addrs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public AddrList getAddrs() {
    return addrs;
  }


  public void setAddrs(AddrList addrs) {
    this.addrs = addrs;
  }


  public Peer ID(String ID) {
    
    this.ID = ID;
    return this;
  }

   /**
   * network ID of a node
   * @return ID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "12D3L7AUwnPXj7odaWGzWcWwWD1mGrB5d5yr6zLrimHcyiZLVWcH", value = "network ID of a node")

  public String getID() {
    return ID;
  }


  public void setID(String ID) {
    this.ID = ID;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Peer peer = (Peer) o;
    return Objects.equals(this.addrs, peer.addrs) &&
        Objects.equals(this.ID, peer.ID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addrs, ID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Peer {\n");
    sb.append("    addrs: ").append(toIndentedString(addrs)).append("\n");
    sb.append("    ID: ").append(toIndentedString(ID)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

