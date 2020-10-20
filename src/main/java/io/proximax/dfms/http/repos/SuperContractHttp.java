/**
 * 
 */
package io.proximax.dfms.http.repos;

import static io.proximax.dfms.utils.HttpUtils.encode;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.google.gson.reflect.TypeToken;

import io.proximax.dfms.ServiceBase;
import io.proximax.dfms.SuperContractServices;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.gen.model.CidListWrap;
import io.proximax.dfms.gen.model.CidWrap;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.http.dtos.ResultListDTO;
import io.proximax.dfms.http.dtos.SuperContractDTO;
import io.proximax.dfms.http.dtos.SuperContractWrapperDTO;
import io.proximax.dfms.model.sc.SuperContract;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * @author tono
 *
 */
public class SuperContractHttp extends HttpRepository<ServiceBase> implements SuperContractServices {

   private static final String URL_DEPLOY = "sc/deploy";
   private static final String URL_EXECUTE = "sc/exec";
   private static final String URL_GET = "sc/get";
   private static final String URL_LS = "sc/ls";
   private static final String URL_RESULTS = "sc/results";
   private static final String URL_EXECUTIONS = "sc/executions";
   private static final String URL_DEACTIVATE = "sc/deactivate";
   
   private static final Type TYPE_SC_LIST = new TypeToken<List<SuperContractDTO>>(){}.getType();

   /**
    * create new instance
    * 
    * @param api the storage API
    * @param apiPath the path to the API on the node
    * @param client the HTTP client to be used to execute requests
    * @param longPollingClient the HTTP client to be used to execute long polling requests
    */
   public SuperContractHttp(ServiceBase api, String apiPath, OkHttpClient client, OkHttpClient longPollingClient) {
      super(api, Optional.of(apiPath), client, longPollingClient);
   }

   @Override
   public Observable<Cid> deploy(Cid driveId, String path) {
      HttpUrl url = buildUrl(URL_DEPLOY, encode(driveId), path).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, CidWrap.class))
            .map(CidWrap::getId)
            .map(Cid::decode);
   }

   @Override
   public Observable<Cid> execute(Cid superContract, BigInteger gas, String functionName, String[] parameters) {
      if (parameters != null) {
         // TODO implement this. how does this get serialized?
         throw new UnsupportedOperationException("parameters are not implemented");
      }
      HttpUrl url = buildUrl(URL_EXECUTE, gas.toString(), functionName).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, CidWrap.class))
            .map(CidWrap::getId)
            .map(Cid::decode);
   }

   @Override
   public Observable<SuperContract> get(Cid superContractId) {
      HttpUrl url = buildUrl(URL_GET, encode(superContractId)).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, SuperContractWrapperDTO.class))
            .map(SuperContractWrapperDTO::getSuperContract)
            .map(SuperContract::fromDto);
   }

   @Override
   public Observable<List<SuperContract>> list(Cid driveId) {
      HttpUrl url = buildUrl(URL_LS).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(this::toSuperContracts)
            .flatMapIterable(list -> list)
            .map(SuperContract::fromDto)
            .toList().toObservable();
   }

   /**
    * parse list of super contract DTOs form JSON string
    * 
    * @param json JSON string
    * @return list of items
    */
   private List<SuperContractDTO> toSuperContracts(String json) {
      return getGson().fromJson(json, TYPE_SC_LIST);
   }
   
   @Override
   public Observable<List<String>> results(Cid transactionId) {
      HttpUrl url = buildUrl(URL_RESULTS, encode(transactionId)).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, ResultListDTO.class))
            .map(ResultListDTO::getIds);
   }

   @Override
   public Observable<List<Cid>> executions() {
      HttpUrl url = buildUrl(URL_EXECUTIONS).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, CidListWrap.class))
            .map(CidListWrap::getIds)
            .flatMapIterable(list -> list)
            .map(Cid::decode)
            .toList().toObservable();
   }

   @Override
   public Completable deactivate(Cid superContractId) {
      HttpUrl url = buildUrl(URL_DEACTIVATE, encode(superContractId)).build();
      return makePostCompletable(url);
   }

}
