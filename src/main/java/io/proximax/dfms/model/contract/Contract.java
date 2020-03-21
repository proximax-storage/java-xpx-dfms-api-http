/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.contract;

import java.math.BigInteger;
import java.util.List;

import io.proximax.cid.Cid;
import io.proximax.dfms.gen.model.ContractDTO;

/**
 * TODO add proper description
 */
public class Contract {
   private final Cid cid;
//   private final PeerId owner;
   private final String owner;
//   private final List<PeerId> members;
   private final List<String> members;
   private final BigInteger duration;
   private final BigInteger created;
//   private final Cid root;
   private final String root;
   private final BigInteger totalSpace;

   /**
    * @param cid CID of the contract
    * @param owner the owner
    * @param members list of members
    * @param duration duration for which the contract is valid TODO blocks or millis
    * @param created timestamp when the contract was created
    * @param root root of the contract TODO what is it?
    * @param totalSpace total space TODO available or consumed
    */
   public Contract(Cid cid, String owner, List<String> members, BigInteger duration, BigInteger created, String root,
         BigInteger totalSpace) {
      this.cid = cid;
      this.owner = owner;
      this.members = members;
      this.duration = duration;
      this.created = created;
      this.root = root;
      this.totalSpace = totalSpace;
   }

   /**
    * @return the cid
    */
   public Cid getCid() {
      return cid;
   }

   /**
    * @return the owner
    */
   public String getOwner() {
      return owner;
   }

   /**
    * @return the members
    */
   public List<String> getMembers() {
      return members;
   }

   /**
    * @return the duration
    */
   public BigInteger getDuration() {
      return duration;
   }

   /**
    * @return the created
    */
   public BigInteger getCreated() {
      return created;
   }

   /**
    * @return the root
    */
   public String getRoot() {
      return root;
   }

   /**
    * @return the totalSpace
    */
   public BigInteger getTotalSpace() {
      return totalSpace;
   }

   /**
    * build instance form the DTO object
    * 
    * @param dto DTO retrieved form the REST API
    * @return contract instance
    */
   public static Contract fromDto(ContractDTO dto) {
      return new Contract(Cid.decode(dto.getDrive()), dto.getOwner(), dto.getReplicators(),
            BigInteger.valueOf(dto.getDuration()), BigInteger.valueOf(dto.getCreated()), dto.getRoot(),
            BigInteger.valueOf(dto.getSpace()));
   }
}
