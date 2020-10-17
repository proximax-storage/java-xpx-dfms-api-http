/**
 * 
 */
package io.proximax.dfms.cucumber;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.libp2p.core.PeerId;
import io.libp2p.core.multiformats.Multiaddr;
import io.proximax.dfms.DFMSClient;
import io.proximax.dfms.DFMSReplicator;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.contract.DriveContract;

/**
 * new instance of context is created for every scenario. This context is used to share state across steps
 * implemented in different steps classes
 */
public class TestContext {
   public static Random RANDOM = new Random(System.nanoTime());

   private DriveContract contract;
   private List<Cid> cidList;
   private String prefix;
   
   private PeerId clientId;
   private List<Multiaddr> clientAddresses;
   
   private DFMSClient client;
   private Map<String, DFMSReplicator> replicators = new LinkedHashMap<>();
   
   public DFMSReplicator getReplicator(String name) {
      return replicators.get(name);
   }
   
   public DFMSReplicator addReplicator(String name, DFMSReplicator replicator) {
      return replicators.put(name, replicator);
   }
   
   public Map<String, DFMSReplicator> getReplicators() {
      return replicators;
   }
   
   /**
    * @return the client
    */
   public DFMSClient getClient() {
      return client;
   }

   /**
    * @param client the client to set
    */
   public void setClient(DFMSClient client) {
      this.client = client;
   }

   /**
    * @return the clientId
    */
   public PeerId getClientId() {
      return clientId;
   }

   /**
    * @param clientId the clientId to set
    */
   public void setClientId(PeerId clientId) {
      this.clientId = clientId;
   }

   /**
    * @return the clientAddresses
    */
   public List<Multiaddr> getClientAddresses() {
      return clientAddresses;
   }

   /**
    * @param clientAddresses the clientAddresses to set
    */
   public void setClientAddresses(List<Multiaddr> clientAddresses) {
      this.clientAddresses = clientAddresses;
   }

   private Map<String, Object> values;

   public Object getObject(String key) {
      return values.get(key);
   }

   public void setObject(String key, Object contract) {
      values.put(key, contract);
   }

   /**
    * @return the contract
    */
   public DriveContract getContract() {
      return contract;
   }

   /**
    * @param contract the contract to set
    */
   public void setContract(DriveContract contract) {
      this.contract = contract;
   }

   /**
    * @return the cidList
    */
   public List<Cid> getCidList() {
      return cidList;
   }

   /**
    * @param cidList the cidList to set
    */
   public void setCidList(List<Cid> cidList) {
      this.cidList = cidList;
   }

   /**
    * @return the prefix
    */
   public String getPrefix() {
      return prefix;
   }

   /**
    * @param prefix the prefix to set
    */
   public void setPrefix(String randomPrefix) {
      this.prefix = randomPrefix;
   }


}
