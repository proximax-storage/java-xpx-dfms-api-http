/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.contract;

/**
 * TODO add proper description
 */
public interface Subscription<T> {
   
   T next();

   void cancel();
}
