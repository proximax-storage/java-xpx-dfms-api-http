/*
 * Copyright 2020 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.contract.DriveContract;
import io.proximax.dfms.model.contract.Invite;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Contract services provided by replicator nodes allowing nodes to join under given contract
 */
public interface ContractReplicatorServices {
   
   /**
    * Start listening for new invites
    * 
    * @return observable producing invites as they arrive on the long polling socket
    */
   Observable<Invite> invites();

   /**
    * Accept invite
    * 
    * @param id CID for the drive for which invite was received
    * @return
    */
   Completable accept(Cid id);

   /**
    * Start listening for accepted contracts
    * 
    * @return observable producing contracts as they are accepted by replicator nodes
    */
   Observable<DriveContract> accepted();
}
