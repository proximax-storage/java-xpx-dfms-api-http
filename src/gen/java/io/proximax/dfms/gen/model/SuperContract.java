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
import java.util.ArrayList;
import java.util.List;

/**
 * SuperContract
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-10-17T14:27:52.840+02:00[Europe/Prague]")
public class SuperContract {
  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private String id;

  public static final String SERIALIZED_NAME_DRIVE = "drive";
  @SerializedName(SERIALIZED_NAME_DRIVE)
  private Contract drive;

  public static final String SERIALIZED_NAME_FILE = "file";
  @SerializedName(SERIALIZED_NAME_FILE)
  private String file;

  public static final String SERIALIZED_NAME_VMVERSION = "vmversion";
  @SerializedName(SERIALIZED_NAME_VMVERSION)
  private Integer vmversion;

  public static final String SERIALIZED_NAME_FUNCTIONS = "functions";
  @SerializedName(SERIALIZED_NAME_FUNCTIONS)
  private List<String> functions = null;


  public SuperContract id(String id) {
    
    this.id = id;
    return this;
  }

   /**
   * [Cid](https://github.com/multiformats/cid) (version 1) - special content identifier. 
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "baegaajaiaqjcahaxr4ry4styn74ronvr2nvfdmgxtrzyhsci2xqpw5eisrisrgn5", value = "[Cid](https://github.com/multiformats/cid) (version 1) - special content identifier. ")

  public String getId() {
    return id;
  }


  public void setId(String id) {
    this.id = id;
  }


  public SuperContract drive(Contract drive) {
    
    this.drive = drive;
    return this;
  }

   /**
   * Get drive
   * @return drive
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Contract getDrive() {
    return drive;
  }


  public void setDrive(Contract drive) {
    this.drive = drive;
  }


  public SuperContract file(String file) {
    
    this.file = file;
    return this;
  }

   /**
   * [Cid](https://github.com/multiformats/cid) (version 1) - special content identifier. 
   * @return file
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "baegaajaiaqjcahaxr4ry4styn74ronvr2nvfdmgxtrzyhsci2xqpw5eisrisrgn5", value = "[Cid](https://github.com/multiformats/cid) (version 1) - special content identifier. ")

  public String getFile() {
    return file;
  }


  public void setFile(String file) {
    this.file = file;
  }


  public SuperContract vmversion(Integer vmversion) {
    
    this.vmversion = vmversion;
    return this;
  }

   /**
   * Get vmversion
   * @return vmversion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1", value = "")

  public Integer getVmversion() {
    return vmversion;
  }


  public void setVmversion(Integer vmversion) {
    this.vmversion = vmversion;
  }


  public SuperContract functions(List<String> functions) {
    
    this.functions = functions;
    return this;
  }

  public SuperContract addFunctionsItem(String functionsItem) {
    if (this.functions == null) {
      this.functions = new ArrayList<>();
    }
    this.functions.add(functionsItem);
    return this;
  }

   /**
   * Get functions
   * @return functions
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<String> getFunctions() {
    return functions;
  }


  public void setFunctions(List<String> functions) {
    this.functions = functions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SuperContract superContract = (SuperContract) o;
    return Objects.equals(this.id, superContract.id) &&
        Objects.equals(this.drive, superContract.drive) &&
        Objects.equals(this.file, superContract.file) &&
        Objects.equals(this.vmversion, superContract.vmversion) &&
        Objects.equals(this.functions, superContract.functions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, drive, file, vmversion, functions);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SuperContract {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    drive: ").append(toIndentedString(drive)).append("\n");
    sb.append("    file: ").append(toIndentedString(file)).append("\n");
    sb.append("    vmversion: ").append(toIndentedString(vmversion)).append("\n");
    sb.append("    functions: ").append(toIndentedString(functions)).append("\n");
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
