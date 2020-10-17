/**
 * 
 */
package io.proximax.dfms.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.proximax.dfms.ContractClientServices;
import io.proximax.dfms.ContractReplicatorServices;
import io.proximax.dfms.DFMSReplicator;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.contract.DriveContract;
import io.proximax.dfms.model.contract.DriveContractDuration;
import io.proximax.dfms.model.contract.DriveContractOptions;

/**
 * @author tono
 *
 */
public class ContractsSteps extends BaseSteps {

   /**
    * @throws MalformedURLException
    * 
    */
   public ContractsSteps(final TestContext testContext) throws MalformedURLException {
      super(testContext);
   }

   @Given("replicators are connected")
   public void replicators_are_connected() {
      connectReplicators();
   }

   @Given("replicators accept contracts")
   public void replicators_accept_contracts() {
      acceptContracts(replicator1);
      acceptContracts(replicator2);
      acceptContracts(replicator3);
   }

   private void acceptContracts(final DFMSReplicator replicator) {
      ContractReplicatorServices conRep = replicator.createContractReplicatorServices();
      conRep.invites().subscribe(invite -> {
         conRep.accept(invite.getDrive()).blockingAwait();
      });
   }

   @When("I compose new contract")
   public void compose_new_contract() {
      // create repositories
      ContractClientServices clientContract = client.createContractClientServices();
      // prepare contract attributes
      BigInteger space = BigInteger.valueOf(1000000l);
      DriveContractDuration duration = DriveContractDuration.ofDays(5);
      DriveContractOptions options = DriveContractOptions.empty();
      // compose contract
      DriveContract composedContract = clientContract.compose(space, duration, options).blockingFirst();
      ctx.setContract(composedContract);
      // make rudimentary checks
      assertEquals(space, composedContract.getSpace());
   }

   @Then("contract is created")
   public void contract_is_created() {
      ContractClientServices clientContract = client.createContractClientServices();
      DriveContract retrievedContract = clientContract.get(ctx.getContract().getId()).blockingFirst();
      assertNotNull(retrievedContract);
   }

   @Given("I can use contract {string} for my test")
   public void contract_is_available(String contractId) {
      ContractClientServices clientContract = client.createContractClientServices();
      DriveContract retrievedContract = clientContract.get(Cid.decode(contractId)).blockingFirst();
      assertNotNull(retrievedContract);
      // store the contract for the use
      ctx.setContract(retrievedContract);
   }

   @When("I query for all contracts")
   public void i_query_for_all_contracts() {
      ContractClientServices clientContract = client.createContractClientServices();
      List<Cid> cids = clientContract.list().blockingFirst();
      ctx.setCidList(cids);
   }

   @Then("the response will contain my contract")
   public void the_response_will_contain_my_contract() {
      Cid cid = ctx.getContract().getId();
      assertTrue(ctx.getCidList().contains(cid));
   }

}
