/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.dfms;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import io.ipfs.cid.Cid;
import io.proximax.dfms.drive.DriveContent;
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
public interface DriveRepository {

   /**
    * adds the file to a specific Drive and to a given path.
    * 
    * @param id
    * @param path
    * @param content
    * @return
    * @throws IOException 
    */
   Observable<Cid> add(Cid id, String path, DriveContent content) throws IOException;

   /**
    * retrieves file from a specific contract at a given path.
    * 
    * @param id
    * @param path
    * @return
    */
   Observable<DriveContent> get(Cid id, String path);

   /**
    * removes reference of the file at a given path from a specific Drive.
    * 
    * @param id
    * @param path
    * @return
    */
   Completable remove(Cid id, String path);

   /**
    * moves file in a specific Drive from one path to another.
    * 
    * @param id
    * @param sourcePath
    * @param destinationPath
    * @return
    */
   Completable move(Cid id, String sourcePath, String destinationPath);

   /**
    * <p>copies file in a specific Drive from one path to another.</p> 
    * <p>NOTE: Does not do actual copy, only copies reference(a.k.a SymLink). That way file is not duplicated on a disk, but accessible from different paths.</p>
    * 
    * @param id
    * @param sourcePath
    * @param destinationPath
    * @return
    */
   Completable copy(Cid id, String sourcePath, String destinationPath);

   /**
    * creates new directory in a specific Drive at a given path.
    * 
    * @param id
    * @param path
    * @return
    */
   Completable makeDir(Cid id, String path);

   /**
    * returns information about a file or directory at a given path of a specific Drive
    * 
    * @param id
    * @param path
    * @return
    */
   Observable<Path> stat(Cid id, String path);

   /**
    *  lists all the files and directories of a specific Drive and information about them at a given path.
    *  
    * @param id
    * @param path
    * @return
    */
   Observable<List<Path>> ls(Cid id, String path);

   /**
    * uploads the state of a Drive to all contract members.
    * 
    * @param id
    * @param path
    * @return
    */
   Completable flush(Cid id, String path);

}
