/**
 * 
 */
package io.proximax.dfms.model.contract;

import java.util.List;

import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.http.dtos.VerifyResultDTO;

/**
 * Verification error identifying replicator and faulty blocks that were detected
 */
public class VerificationError {

   private final PublicKey replicator;
   private final List<Cid> faultyBlocks;

   /**
    * @param replicator
    * @param faultyBlocks
    */
   public VerificationError(PublicKey replicator, List<Cid> faultyBlocks) {
      this.replicator = replicator;
      this.faultyBlocks = faultyBlocks;
   }

   /**
    * @return the replicator
    */
   public PublicKey getReplicator() {
      return replicator;
   }

   /**
    * @return the faultyBlocks
    */
   public List<Cid> getFaultyBlocks() {
      return faultyBlocks;
   }

   public static VerificationError fromDto(VerifyResultDTO dto) {
      // TODO implement actual conversion from the DTO
      return new VerificationError(null, null);
   }
}
