/**
 * 
 */
package io.proximax.dfms.model.contract;

import java.util.List;
import java.util.stream.Collectors;

import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.gen.model.VerifyResult;

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

   public static VerificationError fromDto(VerifyResult dto) {
      List<Cid> faults = dto.getFaultyBlocks().stream().map(Cid::decode).collect(Collectors.toList());
      return new VerificationError(PublicKey.fromHexString(dto.getReplicator()), faults);
   }
}
