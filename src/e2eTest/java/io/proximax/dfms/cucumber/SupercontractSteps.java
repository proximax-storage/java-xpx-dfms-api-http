/**
 * 
 */
package io.proximax.dfms.cucumber;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.MalformedURLException;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.proximax.dfms.SuperContractServices;
import io.proximax.dfms.cid.Cid;

/**
 * @author tono
 *
 */
public class SupercontractSteps extends BaseSteps {

   /**
    * @throws MalformedURLException
    * 
    */
   public SupercontractSteps(final TestContext testContext) throws MalformedURLException {
      super(testContext);
   }

   @When("I deploy the supercontract using prefixed file {string}")
   public void i_deploy_the_supercontract_using_prefixed_file(String fileNameSuffix) {
      SuperContractServices scs = ctx.getClient().createSuperContractServices();
      Cid scCid = scs.deploy(ctx.getContract().getId(), ctx.getPrefixedFile(fileNameSuffix)).blockingFirst();
      ctx.setSupercontractCid(scCid);
   }

   @When("I execute the supercontract function {string} with gas {int}")
   public void i_execute_the_supercontract(String functionName, Integer gas) {
      Cid scCid = ctx.getSupercontractCid();
      SuperContractServices scs = ctx.getClient().createSuperContractServices();
      Cid txCid = scs.execute(scCid, BigInteger.valueOf(gas), functionName).blockingFirst();
      ctx.setTransactionCid(txCid);
   }

   @Then("the supercontract is listed by the node")
   public void the_supercontract_is_listed_by_the_node() {
      Cid scCid = ctx.getSupercontractCid();
      SuperContractServices scs = ctx.getClient().createSuperContractServices();
      Boolean scIsPresent = scs
            .ls(ctx.getContract().getId())
            .flatMapIterable(contracts -> contracts)
            .any(contract -> scCid.equals(contract)).blockingGet();
      assertTrue(scIsPresent);
   }
}
