/**
 * 
 */
package io.proximax.dfms.cucumber;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.libp2p.core.PeerId;
import io.libp2p.core.multiformats.Multiaddr;
import io.libp2p.core.multiformats.Protocol;
import io.proximax.dfms.DFMSBaseNode;
import io.proximax.dfms.model.network.PeerInfo;

/**
 * @author tono
 *
 */
public class NetworkSteps extends BaseSteps {

   /**
    * @throws MalformedURLException
    * 
    */
   public NetworkSteps(final TestContext testContext) throws MalformedURLException {
      super(testContext);
   }

   @When("I retrieve DFMS client ID")
   public void i_retrieve_dfms_client_id() {
      ctx.setClientId(ctx.getClient().createNetworkServices().getId().blockingFirst());
   }

   @When("I retrieve DFMS client addresses")
   public void i_retrieve_dfms_client_addresses() {
      ctx.setClientAddresses(ctx.getClient().createNetworkServices().getAddresses().blockingFirst());
   }

   @Then("DFMS client is present among {string} peers")
   public void dfms_client_is_present_among_peers(String nodeName) {
      assertTrue(isClientPresentAmongPeers(nodeName));
   }

   @Then("DFMS client is not present among {string} peers")
   public void dfms_client_is_not_present_among_peers(String nodeName) {
      assertFalse(isClientPresentAmongPeers(nodeName));

   }

   public boolean isClientPresentAmongPeers(String nodeName) {
      final PeerId clientId = ctx.getClientId();
      final List<Multiaddr> clientAddresses = ctx.getClientAddresses();
      // decide on replicator
      DFMSBaseNode replicator = ctx.getReplicator(nodeName);
      List<PeerInfo> peers = replicator.createNetworkServices().getPeers().blockingFirst();
      return peers.stream()
            .anyMatch(peer -> peer.getId().equals(clientId) && matchAddresses(clientAddresses, peer.getAddresses()));
   }
   
   protected static boolean matchAddresses(List<Multiaddr> clientAddrs, List<Multiaddr> peerAddrs) {
      return toIP4Strings(clientAddrs).containsAll(toIP4Strings(peerAddrs));
   }

   protected static List<String> toIP4Strings(List<Multiaddr> addrs) {
      return addrs.stream().map(addr -> addr.getStringComponent(Protocol.IP4)).filter(addr -> addr != null)
            .collect(Collectors.toList());
   }
   
}
