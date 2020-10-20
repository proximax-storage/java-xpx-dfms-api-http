package io.proximax.dfms.model.drive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.gen.model.Stat;
import io.proximax.dfms.gen.model.Stat.TypeEnum;
import io.proximax.dfms.gen.model.StatCid;

class DriveItemTest {

   private static final String NAME = "testname";
   private static final DriveItemType TYPE = DriveItemType.FILE;
   private static final long SIZE = 987654321l;
   private static final Cid CID = Cid.decode("baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu");

   @Test
   void testPojo() {
      DriveItem item = new DriveItem(NAME, TYPE, SIZE, CID);
      assertEquals(NAME, item.getName());
      assertEquals(TYPE, item.getType());
      assertEquals(SIZE, item.getSize());
      assertEquals(CID, item.getCid());
   }

   @Test
   void testFromDto() {
      Stat dto = new Stat();
      dto.setName(NAME);
      dto.setType(TypeEnum.fromValue(TYPE.getCode()));
      dto.setSize(SIZE);
      StatCid cid = new StatCid();
      cid.setU(CID.toString());
      dto.setCid(cid);
      // resolve model object form the DTO
      DriveItem item = DriveItem.fromDto(dto);
      // make assertions
      assertEquals(NAME, item.getName());
      assertEquals(TYPE, item.getType());
      assertEquals(SIZE, item.getSize());
      assertEquals(CID, item.getCid());
   }
   

   @Test
   void checkEquals() {
      DriveItem a1 = new DriveItem(NAME, TYPE, SIZE, CID);
      DriveItem a2 = new DriveItem(NAME, TYPE, SIZE, CID);
      DriveItem b = new DriveItem(NAME, TYPE, 345345353l, CID);

      assertEquals(a1, a1);
      assertEquals(a1, a2);
      assertNotEquals(a1, b);
      assertNotEquals(a1, null);
      assertNotEquals(a1, "whatever");
   }

   @Test
   void checkHashcode() {
      DriveItem a1 = new DriveItem(NAME, TYPE, SIZE, CID);
      DriveItem a2 = new DriveItem(NAME, TYPE, SIZE, CID);
      DriveItem b = new DriveItem(NAME, TYPE, 345345353l, CID);

      assertEquals(a1.hashCode(), a1.hashCode());
      assertEquals(a1.hashCode(), a2.hashCode());
      assertNotEquals(a1.hashCode(), b.hashCode());
   }

   @Test
   void testToString() {
      DriveItem item = new DriveItem(NAME, TYPE, SIZE, CID);

      assertTrue(item.toString().startsWith("DriveItem"));
   }
}
