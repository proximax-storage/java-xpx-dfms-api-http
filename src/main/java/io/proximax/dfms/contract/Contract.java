/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.contract;

import java.math.BigInteger;
import java.util.List;

import io.proximax.Cid;
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
    * @param cid
    * @param owner
    * @param members
    * @param duration
    * @param created
    * @param root
    * @param totalSpace
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

   public static Contract fromDto(ContractDTO obj) {
      return new Contract(Cid.decode(obj.getDrive()), obj.getOwner(), obj.getReplicators(),
            BigInteger.valueOf(obj.getDuration()), BigInteger.valueOf(obj.getCreated()), obj.getRoot(),
            BigInteger.valueOf(obj.getSpace()));
   }
}
