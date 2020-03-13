package io.proximax.dfms.model.drive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import io.proximax.dfms.model.drive.content.ByteArrayContent;
import io.proximax.dfms.model.drive.content.DirectoryContent;
import io.proximax.dfms.model.drive.content.FileSystemContent;
import io.proximax.dfms.model.drive.content.InputStreamContent;

/**
 * Representation of the hierarchical data in the DFMS. Can represent either some data (e.g. file) or a
 * node in hierarchy (e.g. directory)
 */
public interface DriveContent {
   
   /**
    * get the name of the node
    * 
    * @return the name
    */
   public Optional<String> getName();

   /**
    * check whether this node represents node (does not have content but has children) or content
    * 
    * @return true if node represents directory, false otherwise
    */
   public boolean isNode();

   /**
    * <p>Get input stream with the content of this node.</p>
    * 
    * <p>Note that some implementations (e.g. directory) do not have content directly but in child nodes.
    * In such case this method will throw runtime exception. Use {@linkplain #isNode()} to check whether
    * this node is directory</p>
    * 
    * @return the input stream
    * @throws IOException when content is not available
    */
   public abstract InputStream getInputStream() throws IOException;

   /**
    * get child nodes representing directory-like structure
    * 
    * @return the child nodes
    */
   public abstract List<DriveContent> getChildren();

   /**
    * <p>get byte array representing the content of the node</p>
    * 
    * <p>Note that some implementations might not allow repeated calls to this method. For example when
    * backed by InputStream which will get depleted by call to this method</p>
    * 
    * @return content as byte array
    * @throws IOException when content is not available
    */
   default byte[] toByteArray() throws IOException {
      // copy content from input stream and return as an byte array
      try (InputStream in = getInputStream(); ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
         // use 10kB as buffer to copy content over
         byte[] tmp = new byte[10240];
         int len;
         while ((len = in.read(tmp)) >= 0) {
            bout.write(tmp, 0, len);
         }
         // return the byte array with the content
         return bout.toByteArray();
      }
   }
   
   /**
    * create new content defined as path. Can be directory or file. See {@link FileSystemContent} for details
    * 
    * @param path the path to represent
    * @return the instance of storage content
    */
   public static DriveContent from(Path path) {
      return new FileSystemContent(path);
   }
   
   /**
    * create new content defined by byte array
    * 
    * @param name optional name of the content
    * @param content the actual bytes of the data
    * @return the instance of the storage content
    */
   public static DriveContent from(Optional<String> name, byte[] content) {
      return new ByteArrayContent(name, content);
   }
   
   /**
    * create new content as a directory of other content instances
    * 
    * @param name optional name of the content
    * @param children list of child content instances
    * @return the instance of the storage content
    */
   public static DriveContent from(Optional<String> name, List<DriveContent> children) {
      return new DirectoryContent(name, children);
   }
   
   /**
    * create new content defined by input stream
    * 
    * @param name optional name of the content
    * @param inputStream input stream with content data
    * @return the instance of the storage content
    */
   public static DriveContent from(Optional<String> name, InputStream inputStream) {
      return new InputStreamContent(name, inputStream);
   }
}
