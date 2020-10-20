/*
 * Copyright 2020 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.io.IOException;
import java.util.List;

import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.drive.DriveContent;
import io.proximax.dfms.model.drive.DriveItem;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * <p>
 * DriveAPI is an implementation of DFMS's drive API.
 * </p>
 * <p>
 * Drive is FS like abstraction, which allows Drive contract owners to interact with disks of contract members owner
 * occupies. All FS manipulations are executed locally on the DFMS node to prepare the Drive and all it's data for
 * further transportation and replication. That means client's Drive would make a full copy of the files. Files can be
 * cleared from DFMS node disk after uploading to members. To upload local Drive state to members use Flush.
 * </p>
 */
public interface DriveServices {

   /**
    * adds the content to a specific Drive and to a given path.
    * 
    * @param contract id of the contract under which the content is being uploaded
    * @param path path where content will be placed
    * @param content content to place on the path
    * @return cid of the created content
    * @throws IOException exception when something failed
    */
   Observable<Cid> add(Cid contract, String path, DriveContent content) throws IOException;

   /**
    * retrieves content for a specific contract at a given path. The content is represented as an input stream of a tar
    * file containing the directory
    * 
    * @param contract CID of the contract
    * @param path the path for given contract
    * @return content instance
    */
   Observable<DriveContent> get(Cid contract, String path);

   /**
    * Returns a file from remote nodes or local store by CID. The content is represented as an input stream of a tar
    * file containing the directory
    * 
    * @param contract CID of the contract
    * @param file CID of the file
    * @return content instance
    */
   Observable<DriveContent> file(Cid contract, Cid file);

   /**
    * removes reference of the file at a given path from a specific Drive.
    * 
    * @param contract CID of the contract
    * @param path path to the item
    * @return confirmation that request was fulfilled
    */
   Completable remove(Cid contract, String path);

   /**
    * moves file in a specific Drive from one path to another.
    * 
    * @param contract CID of the contract
    * @param sourcePath path to be moved to new location
    * @param destinationPath destination of the move
    * @return confirmation that request was fulfilled
    */
   Completable move(Cid contract, String sourcePath, String destinationPath);

   /**
    * <p>
    * Copies a file/directory from a source path to a destination path
    * </p>
    * <p>
    * NOTE: Does not do actual copy, only copies reference(a.k.a SymLink). That way file is not duplicated on a disk,
    * but accessible from different paths.
    * </p>
    * 
    * @param contract CID of the contract
    * @param sourcePath path to copy form
    * @param destinationPath path to copy to
    * @return confirmation that request was fulfilled
    */
   Completable copy(Cid contract, String sourcePath, String destinationPath);

   /**
    * creates new directory in a specific Drive at a given path.
    * 
    * @param contract CID of the contract
    * @param path path to the item
    * @return confirmation that request was fulfilled
    */
   Completable makeDir(Cid contract, String path);

   /**
    * returns information about a file or directory at a given path of a specific Drive
    * 
    * @param contract CID of the contract
    * @param path path to the item
    * @return observable drive item
    */
   Observable<DriveItem> stat(Cid contract, String path);

   /**
    * lists all the files and directories of a specific Drive and information about them at a given path.
    * 
    * @param contract CID of the contract
    * @param path path to the item
    * @return observable list of drive items
    */
   Observable<List<DriveItem>> ls(Cid contract, String path);

   /**
    * uploads the state of a Drive to all contract members.
    * 
    * @param contract CID of the contract
    * @param path path to the item
    * @return confirmation that request was fulfilled
    */
   Completable flush(Cid contract, String path);

}
