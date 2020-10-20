/**
 * 
 */
package io.proximax.dfms.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.proximax.dfms.DriveServices;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.DriveItem;
import io.proximax.dfms.model.drive.DriveItemType;
import io.proximax.dfms.model.drive.content.FileSystemContent;

/**
 * @author tono
 *
 */
public class DriveSteps extends BaseSteps {
   public static final String PREFIX = "testDataPrefix";

   /**
    * @throws MalformedURLException
    * 
    */
   public DriveSteps(final TestContext testContext) throws MalformedURLException {
      super(testContext);
   }

   @Given("I generate random prefix")
   public void i_generate_random_prefix() {
      String prefix = PREFIX + TestContext.RANDOM.nextLong();
      ctx.setPrefix(prefix);
   }

   @When("I upload file {string} as prefixed {string}")
   public void i_upload_file_as_prefixed(String sourceFile, String targetName) throws IOException {
      DriveServices drive = ctx.getClient().createDriveServices();
      DriveContent addContent = new FileSystemContent(new File(sourceFile).toPath());
      Cid cid = drive.add(ctx.getContract().getId(), ctx.getPrefix() + targetName, addContent).timeout(30, TimeUnit.SECONDS)
            .blockingFirst();
      assertNotNull(cid);
   }

   @Then("prefixed file {string} has size {long} and cid {string}")
   public void prefixed_file_has_size_and_cid(String driveFile, Long size, String cid) {
      DriveServices drive = ctx.getClient().createDriveServices();
      DriveItem item = drive.stat(ctx.getContract().getId(), ctx.getPrefix() + driveFile).blockingFirst();
      assertEquals(DriveItemType.FILE, item.getType());
      assertEquals(size, item.getSize());
      assertEquals(Cid.decode(cid), item.getCid());

   }
   
   @When("I create prefixed directory {string}")
   public void i_create_prefixed_directory(String dirName) {
      DriveServices drive = ctx.getClient().createDriveServices();
      drive.makeDir(ctx.getContract().getId(), ctx.getPrefix()+dirName).timeout(30, TimeUnit.SECONDS).blockingAwait();
   }

   @Then("file {string} is listed in prefixed directory {string}")
   public void file_is_listed_in_prefixed_directory(String fileName, String dirSuffix) {
       DriveServices fs = ctx.getClient().createDriveServices();
       String prefix = ctx.getPrefix();
       List<DriveItem> items = fs.ls(ctx.getContract().getId(), prefix + dirSuffix).blockingFirst();
       assertTrue(items.stream().map(DriveItem::getName).anyMatch(name -> name.equals(fileName)));
   }

   @Then("{int} file is listed in prefixed directory {string}")
   public void file_is_listed_in_prefixed_directory(Integer count, String dirSuffix) {
      DriveServices fs = ctx.getClient().createDriveServices();
      String prefix = ctx.getPrefix();
      List<DriveItem> items = fs.ls(ctx.getContract().getId(), prefix + dirSuffix).blockingFirst();
      assertEquals(count, items.size());
   }
}
