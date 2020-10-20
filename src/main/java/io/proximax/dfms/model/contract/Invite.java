/**
 * 
 */
package io.proximax.dfms.model.contract;

import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.gen.model.Contract;

/**
 * @author tono
 *
 */
public class Invite {
   private final Cid drive;
   private final PublicKey owner;

   /**
    * @param drive
    * @param owner
    */
   public Invite(Cid drive, PublicKey owner) {
      this.drive = drive;
      this.owner = owner;
   }

   /**
    * @return the drive
    */
   public Cid getDrive() {
      return drive;
   }

   /**
    * @return the owner
    */
   public PublicKey getOwner() {
      return owner;
   }

   /**
    * convert from DTO to actual application model
    * 
    * @param dto source DTO
    * @return new instance
    */
   public static Invite fromDto(Contract dto) {
      return new Invite(Cid.decode(dto.getDrive()), PublicKey.fromHexString(dto.getOwner()));
   }

}
