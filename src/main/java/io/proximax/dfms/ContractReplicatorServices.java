/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.contract.Acceptation;
import io.proximax.dfms.model.contract.Invite;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
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

   Observable<Acceptation> accepted();
}
