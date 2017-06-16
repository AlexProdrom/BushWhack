package bw.bushwhack.domains.profile;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import bw.bushwhack.data.models.Trail;
import bw.bushwhack.data.models.User;
import bw.bushwhack.global.interfaces.OnRetrievingDataListener;
import bw.bushwhack.global.interfaces.Presenter;
import bw.bushwhack.data.ProfileModel;

/**
 * Created by Dmitry on 6/15/2017.
 */

public class ProfilePresenter implements Presenter {

    private static volatile ProfilePresenter mInstance;
    private OnRetrievingDataListener mDataCallback;
    private EventBus mBus = EventBus.getDefault();
    private ProfileModel mModel;

    private ProfilePresenter(){
        this.mModel = new ProfileModel();
        mBus.register(this);
        this.setDatabaseRefs();
    }

    public static ProfilePresenter getInstance(){
        if(mInstance==null){
            synchronized (ProfilePresenter.class){
                if(mInstance == null){
                    mInstance = new ProfilePresenter();
                }
            }
        }
        return mInstance;
    }

    private User mCurrentUser;

    public User getCurrentUser(){
        return mCurrentUser;
    }

    @Subscribe
    public void RetrieveCurrentUser(User user) {
        Log.i("user data",user.toString());
        if (user != null)
            mDataCallback.onCurrentUserRetrieved(user);
        mCurrentUser = user;
    }

    public boolean attemptContinuingATrail(){
        return this.mCurrentUser.getCurrentTrail() != null;
    }

    public void setNewCurrentTrail(String trailReferenceKey){
        mModel.setCurrentTrail(trailReferenceKey, this.getCurrentUser());
    }


    @Override
    public void setCallBack(OnRetrievingDataListener callback) {
        if(callback!=null)
            mDataCallback=callback;
    }

    @Override
    public void setDatabaseRefs() {
        mModel.setCurrentUserRef();
    }

    @Override
    @Subscribe
    public void RetrieveError(Error error) {
        if(error!=null)
            mDataCallback.onErrorOccurance(error);
    }

    @Override
    public void destroy() {
        this.mInstance = null;
    }
}
