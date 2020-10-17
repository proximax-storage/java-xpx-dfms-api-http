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
 * AddrListWrap
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-10-17T01:21:04.340+02:00[Europe/Prague]")
public class AddrListWrap {
  public static final String SERIALIZED_NAME_ADDRS = "Addrs";
  @SerializedName(SERIALIZED_NAME_ADDRS)
  private AddrList addrs = new AddrList();


  public AddrListWrap addrs(AddrList addrs) {
    
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddrListWrap addrListWrap = (AddrListWrap) o;
    return Objects.equals(this.addrs, addrListWrap.addrs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addrs);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddrListWrap {\n");
    sb.append("    addrs: ").append(toIndentedString(addrs)).append("\n");
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

