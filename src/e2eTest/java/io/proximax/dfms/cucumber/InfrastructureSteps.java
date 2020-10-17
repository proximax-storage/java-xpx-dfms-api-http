/**
 * 
 */
package io.proximax.dfms.cucumber;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import io.cucumber.java.en.Given;
import io.libp2p.core.multiformats.Multiaddr;
import io.libp2p.core.multiformats.Protocol;
import io.proximax.dfms.ContractReplicatorServices;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.DFMSReplicator;
import io.proximax.dfms.NetworkServices;

/**
 * @author tono
 *
 */
public class InfrastructureSteps extends BaseSteps {

   /**
    * @throws MalformedURLException
    * 
    */
   public InfrastructureSteps(final TestContext testContext) throws MalformedURLException {
      super(testContext);
   }

   @Given("all replicators are connected")
   public void connectAllReplicators() {
      // prepare network repositories
      NetworkServices netClient = ctx.getClient().createNetworkServices();
      // retrieve internal docker address where DFMS client is running
      final Multiaddr addr = netClient.getAddresses().flatMapIterable(lst -> lst).filter(multiAddr -> multiAddr != null)
            .filter(multiAddr -> {
               String cmp = multiAddr.getStringComponent(Protocol.IP4);
               return cmp!=null && cmp.startsWith("10.10.");
             }).blockingFirst();
      assertNotNull(addr);
      assertTrue(addr.toString().contains("/ip4/10.10."));
      // go over all replicators and connect to client node
      for (DFMSReplicator rep: ctx.getReplicators().values()) {
         rep.createNetworkServices().connect(addr).blockingAwait();
      }
   }
   
   @Given("all replicators accept contracts")
   public void replicators_accept_contracts() {
      for (DFMSReplicator replicator: ctx.getReplicators().values()) {
         ContractReplicatorServices conRep = replicator.createContractReplicatorServices();
         conRep.invites().subscribe(invite -> {
            conRep.accept(invite.getDrive()).blockingAwait();
         });
      }
   }
   
   @Given("standard infrastructure is available")
   public void standard_infrastructure_is_running() throws MalformedURLException {
       ctx.setClient(new DFMSClient(new URL("http://localhost:6366")));
       ctx.addReplicator("replicator1", new DFMSReplicator(new URL("http://localhost:6400")));
       ctx.addReplicator("replicator2", new DFMSReplicator(new URL("http://localhost:6401")));
       ctx.addReplicator("replicator3", new DFMSReplicator(new URL("http://localhost:6402")));
   }
}
