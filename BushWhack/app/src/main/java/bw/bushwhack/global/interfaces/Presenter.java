package bw.bushwhack.global.interfaces;

import bw.bushwhack.global.events.Error;

public interface Presenter {
    void setCallBack(OnRetrievingDataListener callback);
    void setDatabaseRefs();
    void RetrieveError(Error error);
    void destroy();
}
