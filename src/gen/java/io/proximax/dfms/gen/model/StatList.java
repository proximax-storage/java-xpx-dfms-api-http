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
import io.proximax.dfms.gen.model.Stat;
import java.util.ArrayList;
import java.util.List;

/**
 * StatList
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-10-25T18:34:19.638+01:00[Europe/Prague]")
public class StatList extends ArrayList<Stat> {

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatList {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

