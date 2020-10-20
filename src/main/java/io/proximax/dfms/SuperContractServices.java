/**
 * 
 */
package io.proximax.dfms;

import java.math.BigInteger;
import java.util.List;

import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.model.sc.SuperContract;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Interaction with SuperContracts on a drive
 */
public interface SuperContractServices {

   /**
    * Deploy a new SuperContract by a file path
    * 
    * @param driveId CID of the drive/contract
    * @param path path of the file to be executed
    * @return the SuperContract ID
    */
   Observable<Cid> deploy(Cid driveId, String path);
   
   /**
    * Start execution of a SuperContract
    * 
    * @param superContract CID of deployed super contract
    * @param gas A count of gas for execution
    * @param functionName Name of the function to execute
    * @param parameters parameters for the executed function
    * @return transaction ID
    */
   Observable<Cid> execute(Cid superContract, BigInteger gas, String functionName, String[] parameters);
   
   /**
    * Get a SuperContract by ID
    * 
    * @param superContractId CID of deployed super contract
    * @return the super contract descriptor
    */
   Observable<SuperContract> get(Cid superContractId);
   
   /**
    * Get all super contracts on a drive
    * 
    * @param driveId CID of the drive/contract
    * @return observable list of super contracts on a drive
    */
   Observable<List<SuperContract>> list(Cid driveId);
   
   /**
    * Get results of a SuperContract execution by its TxID
    * 
    * @param transactionId CID of the transaction
    * @return observable execution results
    */
   Observable<List<String>> results(Cid transactionId);
   
   /**
    * Get all execution results started by a node. Returns Transaction IDs.
    * 
    * @return observable list of transaction IDs
    */
   Observable<List<Cid>> executions();
   
   /**
    * Deactivate a SuperContract by its ID
    * 
    * @param superContractId CID of deployed super contract
    * @return notification of completion
    */
   Completable deactivate(Cid superContractId);
}
