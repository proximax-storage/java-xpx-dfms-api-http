/**
 * 
 */
package io.proximax.dfms.http.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * @author tono
 *
 */
public class PeerIdDTO {
   @SerializedName("ID")
   private final String id;

   public PeerIdDTO(String id) {
      super();
      this.id = id;
   }

   public String getId() {
      return id;
   }
   
   
}
