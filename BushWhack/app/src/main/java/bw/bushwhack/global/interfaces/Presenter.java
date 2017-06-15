package bw.bushwhack.global.interfaces;

/**
 * Created by Dmitry on 6/15/2017.
 */

public interface Presenter {
    void setCallBack(OnRetrievingDataListener callback);
    void setDatabaseRefs();
    void RetrieveError(Error error);
    void destroy();
}
