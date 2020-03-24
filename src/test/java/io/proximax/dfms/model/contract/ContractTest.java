/**
 * 
 */
package io.proximax.dfms.model.contract;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.proximax.core.crypto.PublicKey;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.gen.model.ContractDTO;

/**
 * {@link Contract} tests
 */
class ContractTest {
   
   @Test
   void testConstructor() {
      Cid id = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
      Cid root = Cid.decode("baegbeibondkkrhxfprzwrlgxxltarqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
      PublicKey owner = PublicKey.fromHexString("08011220201b155bf3ebe4dcca522549a9835a21d010d07e6c354f0df30a0a0504b83f1b");
      List<PublicKey> replicators = Arrays.asList(PublicKey.fromHexString("08011220201b155bf3ebb4dcca522549a9835a21d010d07e6c354f0df30a0a0504b83f1b"));
      Contract c = new Contract(id, owner, replicators, BigInteger.TEN, BigInteger.ONE, root, BigInteger.valueOf(3));
      // make assertions for provided values
      assertEquals(id, c.getId());
      assertEquals(owner, c.getOwner());
      assertEquals(replicators, c.getReplicators());
      assertEquals(BigInteger.TEN, c.getDuration());
      assertEquals(BigInteger.ONE, c.getCreated());
      assertEquals(root, c.getRoot());
      assertEquals(BigInteger.valueOf(3), c.getSpace());
   }

   @Test
   void testFromDto() {
      Cid id = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
      Cid root = Cid.decode("baegbeibondkkrhxfprzwrlgxxltarqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
      PublicKey owner = PublicKey.fromHexString("08011220201b155bf3ebe4dcca522549a9835a21d010d07e6c354f0df30a0a0504b83f1b");
      List<PublicKey> replicators = Arrays.asList(PublicKey.fromHexString("08011220201b155bf3ebb4dcca522549a9835a21d010d07e6c354f0df30a0a0504b83f1b"));
      
      ContractDTO dto = new ContractDTO();
      dto.setDrive("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
      dto.setOwner("08011220201b155bf3ebe4dcca522549a9835a21d010d07e6c354f0df30a0a0504b83f1b");
      dto.setReplicators(Arrays.asList("08011220201b155bf3ebb4dcca522549a9835a21d010d07e6c354f0df30a0a0504b83f1b"));
      dto.setDuration(10l);
      dto.setCreated(1l);
      dto.setRoot("baegbeibondkkrhxfprzwrlgxxltarqhweh2ylhu4hgo5lxjxpqbpfsw2lu");
      dto.setSpace(3l);
      
      Contract c = Contract.fromDto(dto);
      // make assertions for provided values
      assertEquals(id, c.getId());
      assertEquals(owner, c.getOwner());
      assertEquals(replicators, c.getReplicators());
      assertEquals(BigInteger.TEN, c.getDuration());
      assertEquals(BigInteger.ONE, c.getCreated());
      assertEquals(root, c.getRoot());
      assertEquals(BigInteger.valueOf(3), c.getSpace());
   }
}
