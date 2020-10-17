/**
 * 
 */
package io.proximax.dfms.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import io.libp2p.core.multiformats.Multiaddr;
import io.libp2p.core.multiformats.Protocol;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.DFMSReplicator;
import io.proximax.dfms.NetworkServices;
import io.reactivex.Observable;

/**
 * base implementation for cucumber steps. Provides infrastructure to connect to nodes
 */
public abstract class BaseSteps {
   protected final TestContext ctx;

   protected final DFMSClient client;
   protected final DFMSReplicator replicator1;
   protected final DFMSReplicator replicator2;
   protected final DFMSReplicator replicator3;

   public BaseSteps(final TestContext testContext) throws MalformedURLException {
      this.ctx = testContext;
      client = new DFMSClient(new URL("http://localhost:6366"));
      replicator1 = new DFMSReplicator(new URL("http://localhost:6400"));
      replicator2 = new DFMSReplicator(new URL("http://localhost:6401"));
      replicator3 = new DFMSReplicator(new URL("http://localhost:6402"));
   }

   public void connectReplicators() {
      // prepare network repositories
      NetworkServices netClient = client.createNetworkServices();
      NetworkServices netRep1 = replicator1.createNetworkServices();
      NetworkServices netRep2 = replicator2.createNetworkServices();
      NetworkServices netRep3 = replicator3.createNetworkServices();
      // retrieve internal docker address where DFMS client is running
      Multiaddr addr = netClient.getAddresses().flatMapIterable(lst -> lst).filter(multiAddr -> multiAddr != null)
            .filter(multiAddr -> {
               String cmp = multiAddr.getStringComponent(Protocol.IP4);
               return cmp!=null && cmp.startsWith("10.10.");
             }).blockingFirst();
      assertNotNull(addr);
      assertTrue(addr.toString().contains("/ip4/10.10."));
      // connect all replicators to the DFMS node
      netRep1.connect(addr).blockingAwait();
      netRep2.connect(addr).blockingAwait();
      netRep3.connect(addr).blockingAwait();
      // this is a bit lazy... check that 3 replicators have the DFMS client 3 times in total as peer
      long count = Observable.merge(netRep1.getPeers(), netRep2.getPeers(), netRep3.getPeers()).flatMapIterable(l -> l)
            .flatMapIterable(info -> info.getAddresses())
            .filter(multiAddr -> sameProtocol(addr, multiAddr, Protocol.IP4)
                  && sameProtocol(addr, multiAddr, Protocol.TCP))
            .count().blockingGet();
      assertEquals(3, count);
   }
   
   static boolean sameProtocol(Multiaddr ma1, Multiaddr ma2, Protocol protocol) {
      return ma1.getStringComponent(protocol).equals(ma2.getStringComponent(protocol));
   }
   
}
