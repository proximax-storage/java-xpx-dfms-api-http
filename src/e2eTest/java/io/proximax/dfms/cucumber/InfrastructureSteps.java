/**
 * 
 */
package io.proximax.dfms.cucumber;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.libp2p.core.multiformats.Multiaddr;
import io.libp2p.core.multiformats.Protocol;
import io.proximax.dfms.ContractReplicatorServices;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.DFMSReplicator;
import io.proximax.dfms.NetworkServices;
import io.proximax.dfms.cucumber.config.TestConfig;
import io.proximax.dfms.cucumber.config.TestConfig.Node;

/**
 * infrastructure steps required to define what nodes we are using for tests
 */
public class InfrastructureSteps extends BaseSteps {

   /**
    * @throws MalformedURLException
    * 
    */
   public InfrastructureSteps(final TestContext testContext) throws MalformedURLException {
      super(testContext);
   }

   /**
    * connect all replicators to the client node
    */
   @Given("all replicators are connected")
   public void connectAllReplicators() {
      final Multiaddr addr = getClientAddress();
      // go over all replicators and connect to client node
      for (DFMSReplicator rep : ctx.getReplicators().values()) {
         rep.createNetworkServices().connect(addr).blockingAwait();
      }
   }

   @When("replicator {string} is disconnected from DFMS client")
   public void replicator_is_disconnected_from_dfms_client(String replicatorName) {
      final Multiaddr addr = getClientAddress();
      DFMSReplicator replicator = ctx.getReplicator(replicatorName);
      replicator.createNetworkServices().disconnect(addr).blockingAwait();
   }

   @When("replicator {string} is connected to DFMS client")
   public void replicator_is_connected_to_dfms_client(String replicatorName) {
      final Multiaddr addr = getClientAddress();
      DFMSReplicator replicator = ctx.getReplicator(replicatorName);
      replicator.createNetworkServices().connect(addr).blockingAwait();
   }

   /**
    * retrieve DFMS client IPv4 address
    * 
    * @return the client address
    */
   protected Multiaddr getClientAddress() {
      // prepare network repositories
      NetworkServices netClient = ctx.getClient().createNetworkServices();
      // retrieve internal docker address where DFMS client is running
      final Multiaddr addr = netClient.getAddresses().flatMapIterable(lst -> lst).filter(multiAddr -> multiAddr != null)
            .filter(multiAddr -> {
               // look for IPv4 address which is not the loop-back interface
               String cmp = multiAddr.getStringComponent(Protocol.IP4);
               return cmp != null && !cmp.startsWith("127.0.0.1");
            }).blockingFirst();
      assertNotNull(addr);
      return addr;
   }

   /**
    * configure all replicators to accept invites
    */
   @Given("all replicators accept contracts")
   public void replicators_accept_contracts() {
      for (DFMSReplicator replicator : ctx.getReplicators().values()) {
         ContractReplicatorServices conRep = replicator.createContractReplicatorServices();
         // automatically accept all contracts
         conRep.invites().subscribe(invite -> {
            conRep.accept(invite.getDrive()).blockingAwait();
         });
         // TODO keep track of contracts that were accepted
      }
   }

   @Then("contract was accepted by all replicators")
   public void contract_was_accepted_by_all_replicators() {
       // TODO make sure that we got notification on accepted endpoint for all replicators
       throw new io.cucumber.java.PendingException();
   }
   
   /**
    * load configuration from "src/e2eTest/resources/test_config.json"
    * 
    * @throws IOException when the config load fails
    */
   @Given("standard infrastructure is available")
   public void standard_infrastructure_is_running() throws IOException {
      TestConfig config = TestConfig.load();
      // store the configuration in context
      ctx.setConfiguration(config);
      // create node instances based on the configuration
      for (Node node : config.getNodes()) {
         switch (node.getType()) {
         case CLIENT:
            clientIsAvailableOnUrl(node.getUrl());
            break;
         case REPLICATOR:
            replicatorIsAvailableOnUrl(node.getName(), node.getUrl());
            break;
         default:
            throw new IllegalStateException("Unknown node type " + node.getType());
         }
      }
      // connect the replicators and client
      connectAllReplicators();
   }

   /**
    * register new replicator node with given name and URL
    * 
    * @param name replicator name
    * @param url replicator URL
    * 
    * @throws MalformedURLException when URL is not valid
    */
   @Given("replicator {string} is available on {string}")
   public void replicatorIsAvailableOnUrl(String name, String url) throws MalformedURLException {
      ctx.addReplicator(name, new DFMSReplicator(new URL(url)));
   }

   /**
    * register new client node with given URL
    * 
    * @param url replicator URL
    * 
    * @throws MalformedURLException when URL is not valid
    */
   @Given("client is available on {string}")
   public void clientIsAvailableOnUrl(String url) throws MalformedURLException {
      ctx.setClient(new DFMSClient(new URL(url)));
   }
}
