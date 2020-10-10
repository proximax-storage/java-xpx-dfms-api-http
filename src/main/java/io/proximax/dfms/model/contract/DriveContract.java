/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.contract;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.gen.model.ContractDTO;

/**
 * Representation of drive contract. DriveContract is an agreement between client and replicator peers on some amount of disk
 * Space
 */
public class DriveContract {
   private final Cid id;
   private final PublicKey owner;
   private final List<PublicKey> replicators;
   private final BigInteger duration;
   private final BigInteger created;
   private final Cid root;
   private final BigInteger space;

   /**
    * @param id CID identifier of DriveContract's Drive
    * @param owner owner of the DriveContract. Has write access on the DriveContract's Drive and can control it's state.
    * @param replicators nodes which are responsible for replication of DriveFS
    * @param duration duration for which the contract is valid TODO blocks or millis
    * @param created block height when the DriveContract was started.
    * @param root CID of Drive's top-level directory. It is used as an entry point to access DriveFS
    * @param space total physical Space used by Drive on replicator nodes.
    */
   public DriveContract(Cid id, PublicKey owner, List<PublicKey> replicators, BigInteger duration, BigInteger created,
         Cid root, BigInteger space) {
      this.id = id;
      this.owner = owner;
      this.replicators = replicators;
      this.duration = duration;
      this.created = created;
      this.root = root;
      this.space = space;
   }

   /**
    * @return the id
    */
   public Cid getId() {
      return id;
   }

   /**
    * @return the owner
    */
   public PublicKey getOwner() {
      return owner;
   }

   /**
    * @return the members
    */
   public List<PublicKey> getReplicators() {
      return replicators;
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
   public Cid getRoot() {
      return root;
   }

   /**
    * @return the totalSpace
    */
   public BigInteger getSpace() {
      return space;
   }

   /**
    * build instance form the DTO object
    * 
    * @param dto DTO retrieved form the REST API
    * @return contract instance
    */
   public static DriveContract fromDto(ContractDTO dto) {
      // map the data
      Cid id = Cid.decode(dto.getDrive());
      PublicKey owner = PublicKey.fromHexString(dto.getOwner());
      List<PublicKey> replicators = dto.getReplicators().stream().map(PublicKey::fromHexString)
            .collect(Collectors.toList());
      Cid root = Cid.decode(dto.getRoot());
      // create the instance
      return new DriveContract(id, owner, replicators, BigInteger.valueOf(dto.getDuration()),
            BigInteger.valueOf(dto.getCreated()), root, BigInteger.valueOf(dto.getSpace()));
   }
}
