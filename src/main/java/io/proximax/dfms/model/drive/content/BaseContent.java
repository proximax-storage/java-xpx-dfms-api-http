/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.model.drive.content;

import java.util.Optional;

import io.proximax.dfms.model.drive.DriveContent;

/**
 * TODO add proper description
 */
public abstract class BaseContent implements DriveContent {

   private final Optional<String> name;
   private final boolean isNode;

   /**
    * @param name
    * @param isNode
    */
   public BaseContent(Optional<String> name, boolean isNode) {
      this.name = name;
      this.isNode = isNode;
   }

   /**
    * get the name of the node
    * 
    * @return the name
    */
   @Override
   public Optional<String> getName() {
      return name;
   }

   /**
    * check whether this node represents node (does not have content but has children) or content
    * 
    * @return true if node represents directory, false otherwise
    */
   @Override
   public boolean isNode() {
      return isNode;
   }

}
