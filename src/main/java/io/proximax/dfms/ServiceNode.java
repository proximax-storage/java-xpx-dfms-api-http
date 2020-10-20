/*
 * Copyright 2020 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.net.URL;

/**
 * Common interface for all nodes providing services
 */
public interface ServiceNode {

   /**
    * @return the URL of the node
    */
   URL getUrl();
}
