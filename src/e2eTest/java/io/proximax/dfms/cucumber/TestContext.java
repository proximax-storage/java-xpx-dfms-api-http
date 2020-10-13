/**
 * 
 */
package io.proximax.dfms.cucumber;

import java.util.List;
import java.util.Map;
import java.util.Random;

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
