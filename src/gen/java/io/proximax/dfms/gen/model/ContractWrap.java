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
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.proximax.dfms.gen.model.Contract;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * Wrapper for single drive contract
 */
@ApiModel(description = "Wrapper for single drive contract")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-10-17T12:21:37.505+02:00[Europe/Prague]")
public class ContractWrap {
  public static final String SERIALIZED_NAME_CONTRACT = "Contract";
  @SerializedName(SERIALIZED_NAME_CONTRACT)
  private Contract contract;


  public ContractWrap contract(Contract contract) {
    
    this.contract = contract;
    return this;
  }

   /**
   * Get contract
   * @return contract
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Contract getContract() {
    return contract;
  }


  public void setContract(Contract contract) {
    this.contract = contract;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractWrap contractWrap = (ContractWrap) o;
    return Objects.equals(this.contract, contractWrap.contract);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contract);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractWrap {\n");
    sb.append("    contract: ").append(toIndentedString(contract)).append("\n");
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

