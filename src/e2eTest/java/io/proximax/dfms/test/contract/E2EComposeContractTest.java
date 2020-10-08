/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms.test.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.libp2p.core.multiformats.Multiaddr;
import io.libp2p.core.multiformats.Protocol;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.dfms.ContractClientServices;
import io.proximax.dfms.ContractReplicatorServices;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.DFMSReplicator;
import io.proximax.dfms.NetworkServices;
import io.proximax.dfms.model.contract.Contract;
import io.proximax.dfms.model.contract.ContractDuration;
import io.proximax.dfms.model.contract.ContractOptions;
import io.reactivex.Observable;

/**
 * integration tests for contract composition. See {@link #init()} for URLs where test expects nodes to be running
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class E2EComposeContractTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EComposeContractTest.class);

   private DFMSClient client;
   private DFMSReplicator replicator1;
   private DFMSReplicator replicator2;
   private DFMSReplicator replicator3;
   
   @BeforeAll
   void init() throws MalformedURLException {
      client = new DFMSClient(new URL("http://localhost:6366"));
      replicator1 = new DFMSReplicator(new URL("http://localhost:6400"));
      replicator2 = new DFMSReplicator(new URL("http://localhost:6401"));
      replicator3 = new DFMSReplicator(new URL("http://localhost:6402"));
   }

   @Test
   void step01ConnectReplicators() {
      // prepare network repositories
      NetworkServices netClient = client.createNetworkServices();
      NetworkServices netRep1 = replicator1.createNetworkServices();
      NetworkServices netRep2 = replicator2.createNetworkServices();
      NetworkServices netRep3 = replicator3.createNetworkServices();
      // retrieve internal docker address where DFMS client is running
      Multiaddr addr = netClient.getAddresses()
         .flatMapIterable(lst -> lst)
         .filter(multiAddr -> multiAddr != null)
         .filter(multiAddr -> multiAddr.getStringComponent(Protocol.IP4).startsWith("10.10."))
         .blockingFirst();
      assertNotNull(addr);
      assertTrue(addr.toString().contains("/ip4/10.10."));
      // connect all replicators to the DFMS node
      netRep1.connect(addr).blockingAwait();
      netRep2.connect(addr).blockingAwait();
      netRep3.connect(addr).blockingAwait();
      // this is a bit lazy... check that 3 replicators have the DFMS client 3 times in total as peer
      long count = Observable.merge(netRep1.getPeers(), netRep2.getPeers(), netRep3.getPeers())
         .flatMapIterable(l -> l)
         .flatMapIterable(info -> info.getAddresses())
         .filter(multiAddr -> sameProtocol(addr, multiAddr, Protocol.IP4) && sameProtocol(addr, multiAddr, Protocol.TCP))
         .count().blockingGet();
      assertEquals(3, count);
   }
   
   static boolean sameProtocol(Multiaddr ma1, Multiaddr ma2, Protocol protocol) {
      return ma1.getStringComponent(protocol).equals(ma2.getStringComponent(protocol));
   }
   
   @Test
   void step02WaitForInvites() {
      acceptContracts(replicator1);
      acceptContracts(replicator2);
      acceptContracts(replicator3);
   }
   
   private static void acceptContracts(final DFMSReplicator replicator) {
      ContractReplicatorServices conRep = replicator.createContractReplicatorServices();
      conRep.invites().subscribe(invite -> {
         logger.info("Got invite {}", invite);
         conRep.accept(invite.getDrive()).blockingAwait();
         });
   }
   
   @Test
   void step03ComposeContract() throws MalformedURLException {
      // create repositories
      ContractClientServices clientContract = client.createContractClientServices();
      // prepare contract attributes
      int replicas = 3;
      int minReplicators = 3;
      BigInteger space = BigInteger.valueOf(1000000l);
      ContractDuration duration = ContractDuration.ofDays(5);
      PrivateKey privateKey = PrivateKey
            .fromHexString("2C8178EF9ED7A6D30ABDC1E4D30D68B05861112A98B1629FBE2C8D16FDE97A1C");
      ContractOptions options = new ContractOptions.Builder().replicas(replicas).minReplicators(minReplicators)
            .subscriptionPriceBySpace(space).privateKey(privateKey).build();
      // compose contract
      Contract contract = clientContract.compose(space, duration, options).blockingFirst();
      logger.info("got contract {}", contract);
      assertEquals(space, contract.getSpace());
   }
}
