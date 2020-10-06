/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.http.dtos.InviteDTO;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 */
public interface ReplicatorRepository {
   /**
    * @return
    */
   Observable<InviteDTO> invites();

   /**
    * @param id
    * @return
    */
   Completable accept(Cid id);

}
