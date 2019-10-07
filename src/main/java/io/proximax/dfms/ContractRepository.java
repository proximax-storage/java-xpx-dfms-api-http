/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.math.BigInteger;
import java.time.Duration;

import io.ipfs.cid.Cid;
import io.proximax.dfms.contract.ContractResponse;
import io.proximax.dfms.contract.InviteSubscription;
import io.proximax.dfms.contract.UpdatesSubscription;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * <p>
 * ContractRepository defines DFMS's Contract API
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
public interface ContractRepository {

   /**
    * retrieves Contract by it's content id.
    * 
    * @param id
    * @return
    */
   Observable<ContractResponse> get(Cid id);

   /**
    * <p>
    * accepts Drive contract invitation by content id.
    * </p>
    * <p>
    * Note: Operation is non reversible, once join Node must follow the contract.
    * </p>
    * 
    * @param id
    * @return
    */
   Completable join(Cid id);

   /**
    * List Drive contracts the node has relation with.
    * 
    * @param id
    * @return
    */
   Observable<Cid> list(Cid id);

   /**
    * creates new subscription for updates of specific Drive contract
    * 
    * @param id
    * @return
    */
   Observable<UpdatesSubscription> updates(Cid id);

   /**
    * create new subscription for Drive contract invitation from the network
    * 
    * @return
    */
   Observable<InviteSubscription> invites();

   /**
    * <p>
    * constructs new Drive contract from specific parameters.
    * </p>
    * <p>
    * It announces invitation on the network and waits till minimum(2) amount of nodes Join the invitation. Compose does
    * not guarantee successful completion and may error if minimum(2) amount of nodes have not found through timeout.
    * </p>
    * 
    * @param space
    * @param duration
    * @return
    */
   Observable<ContractResponse> compose(BigInteger space, Duration duration);
   
   /**
    * triggers node to automatically accept incoming contracts.
    * 
    * @return
    */
   Completable startAccepting();
   
   /**
    * stops accepting process.
    * 
    * @return
    */
   Completable stopAccepting();
   
   
}
