/**
 * 
 */
package io.proximax.dfms.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @author tono
 *
 */
public class TestConfig {
   private static final String CONFIG_FILE = "test_config.json";

   @SerializedName("nodes")
   private List<Node> nodes;
   
   /**
    * @return the nodes
    */
   public List<Node> getNodes() {
      return nodes;
   }

   /**
    * @param nodes the nodes to set
    */
   public void setNodes(List<Node> nodes) {
      this.nodes = nodes;
   }

   /**
    * load configuration from a test_config.json file on a classpath
    * 
    * @return config instance
    * @throws IOException when file can not be loaded
    */
   public static TestConfig load() throws IOException {
      try (InputStream propInputStream = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            Reader configReader = new InputStreamReader(propInputStream)) {
         return new Gson().fromJson(configReader, TestConfig.class);
      }
   }
   
   /**
    * Node descriptor
    */
   public static class Node {
      @SerializedName("name")
      private String name;
      @SerializedName("type")
      private NodeType type;
      @SerializedName("url")
      private String url;
      /**
       * @return the name
       */
      public String getName() {
         return name;
      }
      /**
       * @param name the name to set
       */
      public void setName(String name) {
         this.name = name;
      }
      /**
       * @return the url
       */
      public String getUrl() {
         return url;
      }
      /**
       * @param url the url to set
       */
      public void setUrl(String url) {
         this.url = url;
      }
      /**
       * @return the type
       */
      public NodeType getType() {
         return type;
      }
      /**
       * @param type the type to set
       */
      public void setType(NodeType type) {
         this.type = type;
      }
      
   }
   
   /**
    * Type of the node
    */
   public enum NodeType {
      @SerializedName("client")
      CLIENT,
      @SerializedName("replicator")
      REPLICATOR;
   }
}
