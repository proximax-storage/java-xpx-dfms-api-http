/**
 * 
 */
package io.proximax.dfms.model.sc;

import java.util.List;

import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.contract.DriveContract;

/**
 * @author tono
 *
 */
public class SuperContract {
   private final Cid id;
   private final DriveContract drive;
   private final Cid file;
   private final Integer vmversion;
   private final List<String> functions;

   /**
    * @param id
    * @param drive
    * @param file
    * @param vmversion
    * @param functions
    */
   public SuperContract(Cid id, DriveContract drive, Cid file, Integer vmversion, List<String> functions) {
      this.id = id;
      this.drive = drive;
      this.file = file;
      this.vmversion = vmversion;
      this.functions = functions;
   }

   /**
    * @return the id
    */
   public Cid getId() {
      return id;
   }

   /**
    * @return the drive
    */
   public DriveContract getDrive() {
      return drive;
   }

   /**
    * @return the file
    */
   public Cid getFile() {
      return file;
   }

   /**
    * @return the vmversion
    */
   public Integer getVmversion() {
      return vmversion;
   }

   /**
    * @return the functions
    */
   public List<String> getFunctions() {
      return functions;
   }

   /**
    * create supercontract instance from the DTO
    * 
    * @param dto
    * @return
    */
   public static SuperContract fromDto(io.proximax.dfms.gen.model.SuperContract dto) {
      return new SuperContract(Cid.decode(dto.getId()), DriveContract.fromDto(dto.getDrive()),
            Cid.decode(dto.getFile()), dto.getVmversion(), dto.getFunctions());
   }
}
