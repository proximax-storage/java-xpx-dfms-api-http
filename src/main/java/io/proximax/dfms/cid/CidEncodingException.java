/**
 * 
 */
package io.proximax.dfms.cid;

/**
 * Exception indicating that encoding failed
 */
public class CidEncodingException  extends RuntimeException {
   private static final long serialVersionUID = -1092275854036133476L;

   public CidEncodingException(String message) {
      super(message);
   }
   
   public CidEncodingException(String message, Throwable cause) {
      super(message, cause);
   }
}
