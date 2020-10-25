/**
 * 
 */
package io.proximax.dfms.http.repos;

import static io.proximax.dfms.utils.HttpUtils.encode;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.proximax.dfms.ServiceBase;
import io.proximax.dfms.SuperContractServices;
import io.proximax.dfms.cid.Cid;
import io.proximax.dfms.gen.model.CidListWrap;
import io.proximax.dfms.gen.model.CidResultWrap;
import io.proximax.dfms.gen.model.CidWithPath;
import io.proximax.dfms.gen.model.ExecutionsWrap;
import io.proximax.dfms.gen.model.ResultsWrap;
import io.proximax.dfms.gen.model.SuperContractWrap;
import io.proximax.dfms.gen.model.SupercontractExec;
import io.proximax.dfms.http.HttpRepository;
import io.proximax.dfms.model.sc.SuperContract;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * HTTP implementation of supercontract services
 */
public class SuperContractHttp extends HttpRepository<ServiceBase> implements SuperContractServices {

   private static final String URL_DEPLOY = "sc/deploy";
   private static final String URL_EXECUTE = "sc/exec";
   private static final String URL_GET = "sc/get";
   private static final String URL_LS = "sc/ls";
   private static final String URL_RESULTS = "sc/results";
   private static final String URL_EXECUTIONS = "sc/executions";
   private static final String URL_DEACTIVATE = "sc/deactivate";

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
            .map(str -> getGson().fromJson(str, CidResultWrap.class))
            .map(CidResultWrap::getResult)
            .map(Cid::decode);
   }

   @Override
   public Observable<Cid> execute(Cid superContract, BigInteger gas, String functionName, String... parameters) {
      if (parameters != null && parameters.length > 0) {
         // parameters are not implemented on server yet according to dev team
         throw new UnsupportedOperationException("parameters are not functional yet. do not specify them in the call!");
      }
      HttpUrl url = buildUrl(URL_EXECUTE, encode(superContract), gas.toString(), functionName).build();
      // make the request
      return makePostObservable(url, true)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, SupercontractExec.class))
            .map(SupercontractExec::getScId)
            .map(Cid::decode);
   }

   @Override
   public Observable<SuperContract> get(Cid superContractId) {
      HttpUrl url = buildUrl(URL_GET, encode(superContractId)).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, SuperContractWrap.class))
            .map(SuperContractWrap::getSuperContract)
            .map(SuperContract::fromDto);
   }

   @Override
   public Observable<List<Cid>> ls(Cid driveId) {
      HttpUrl url = buildUrl(URL_LS, encode(driveId)).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, CidListWrap.class))
            .map(CidListWrap::getIds)
            .flatMapIterable(list -> list)
            .map(Cid::decode)
            .toList()
            .toObservable();
   }

   @Override
   public Observable<List<String>> results(Cid transactionId) {
      HttpUrl url = buildUrl(URL_RESULTS, encode(transactionId)).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, ResultsWrap.class))
            .map(ResultsWrap::getResults);
   }

   @Override
   public Observable<List<Cid>> executions() {
      HttpUrl url = buildUrl(URL_EXECUTIONS).build();
      // make the request
      return makePostObservable(url, false)
            .map(this::mapStringOrError)
            .map(str -> getGson().fromJson(str, ExecutionsWrap.class))
            .map(ExecutionsWrap::getIds)
            .flatMapIterable(list -> list)
            .map(CidWithPath::getU)
            .map(Cid::decode)
            .toList()
            .toObservable();
   }

   @Override
   public Completable deactivate(Cid superContractId) {
      HttpUrl url = buildUrl(URL_DEACTIVATE, encode(superContractId)).build();
      return makePostObservable(url, false).map(this::mapStringOrError).ignoreElements();
   }

}
