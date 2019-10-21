/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.contract;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonObject;

import io.ipfs.cid.Cid;
import io.libp2p.core.PeerId;

/**
 * TODO add proper description
 */
public class Contract {
   private final Cid cid;
   private final PeerId owner;
   private final List<PeerId> members;
   private final Duration duration;
   private final Date created;
   private final Cid root;
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
   public Contract(Cid cid, PeerId owner, List<PeerId> members, Duration duration, Date created, Cid root,
         BigInteger totalSpace) {
      this.cid = cid;
      this.owner = owner;
      this.members = Collections.unmodifiableList(members);
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
   public PeerId getOwner() {
      return owner;
   }

   /**
    * @return the members
    */
   public List<PeerId> getMembers() {
      return members;
   }

   /**
    * @return the duration
    */
   public Duration getDuration() {
      return duration;
   }

   /**
    * @return the created
    */
   public Date getCreated() {
      return created;
   }

   /**
    * @return the root
    */
   public Cid getRoot() {
      return root;
   }

   /**
    * @return the totalSpace
    */
   public BigInteger getTotalSpace() {
      return totalSpace;
   }
   
   public static Contract fromDto(JsonObject obj) {
      // TODO implement this
      throw new UnsupportedOperationException("not implemented yet");
   }
}
