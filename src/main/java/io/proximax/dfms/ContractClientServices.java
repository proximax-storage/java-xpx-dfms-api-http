/*
 * Copyright 2020 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.math.BigInteger;
import java.util.List;

import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.contract.Amendment;
import io.proximax.dfms.model.contract.DriveContract;
import io.proximax.dfms.model.contract.DriveContractDuration;
import io.proximax.dfms.model.contract.DriveContractOptions;
import io.proximax.dfms.model.contract.VerificationError;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * <p>
 * ContractClientServices defines DFMS's DriveContract API
 * </p>
 * <p>
 * Drive contracts are contracts between network peers for physical disk space. The API allows to create, join, invite,
 * listen for contract updates and invitations. After creating a Drive contract an owner will be able to use disk space
 * of members through DriveAPI.
 * </p>
 * <p>
 * NOTE: Currently creates Drive contracts with DFMS(R) identity as owner.
 * </p>
 */
public interface ContractClientServices {

   /**
    * <p>
    * constructs new Drive contract from specific parameters.
    * </p>
    * <p>
    * It announces invitation on the network and waits till minimum amount of nodes Join the invitation. Compose does
    * not guarantee successful completion and may error if minimum amount of nodes have not found through timeout.
    * </p>
    * <p>
    * Compose synchronously announces invites to the Network with current node as an owner and tries to find members
    * which agrees on specified parameters and options. It does not guarantee success on resolving members. On success
    * persists contract locally and gives ability to use DriveServices.
    * </p>
    * 
    * @param space total space reserved by Drive contract on member nodes. TODO in bytes?
    * @param duration duration of the contract
    * @param options the contract options
    * @return the contract created based on the request
    */
   Observable<DriveContract> compose(BigInteger space, DriveContractDuration duration, DriveContractOptions options);

   /**
    * Searches for Drive contract information in local storage and/or in blockchain.
    * 
    * @param id CID of the Drive contract
    * @return the contract information
    */
   Observable<DriveContract> get(Cid id);

   /**
    * Lists all the contracts in which Node participates as an owner or member
    * 
    * @return observable list of CIDs
    */
   Observable<List<Cid>> list();

   /**
    * Creates subscription for Drive contract corrections for contract in local storage and/or in blockchain.
    * 
    * @param id CID of the Drive contract
    * @return TODO stream of contracts??
    */
   Observable<Amendment> amendments(Cid id);
   
   /**
    * Initiate verification round between replicators.
    * 
    * @param id CID of the Drive contract
    * @return observable list of replicators which failed verification and were excluded from contract immediately
    */
   Observable<List<VerificationError>> verify(Cid id);
   
   /**
    * Finish contract.
    * 
    * @param id CID of the Drive contract
    * @return observable completion
    */
   Completable finish(Cid id);

}
