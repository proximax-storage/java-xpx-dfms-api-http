/**
 * 
 */
package io.proximax.dfms.cucumber;

import java.net.MalformedURLException;

/**
 * base implementation for cucumber steps. Provides infrastructure to connect to nodes
 */
public abstract class BaseSteps {
   protected final TestContext ctx;

   public BaseSteps(final TestContext testContext) throws MalformedURLException {
      this.ctx = testContext;
   }
}
