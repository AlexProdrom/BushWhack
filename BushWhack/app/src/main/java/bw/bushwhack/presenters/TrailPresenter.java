package bw.bushwhack.presenters;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bw.bushwhack.interfaces.OnRetrievingDataListener;
import bw.bushwhack.models.DataModel;
import bw.bushwhack.models.Location;
import bw.bushwhack.models.Marker;
import bw.bushwhack.models.Trail;
import bw.bushwhack.models.User;
import bw.bushwhack.views.CurrentTrailView;

/**
 * Created by prodromalex on 6/3/2017.
 */

public class TrailPresenter {
    private volatile static TrailPresenter uniqueInstance;

    public static TrailPresenter getInstance() {
        if (uniqueInstance == null) {
            synchronized (TrailPresenter.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TrailPresenter(new DataModel(), new CurrentTrailView());
                }
            }
        }
        return uniqueInstance;
    }

    public User getCurrentUser(){
        return mCurrentUser;
    }
    public List<User> getUsers(){
        return mUsers;
    }

    private DataModel mModel;
    private CurrentTrailView mView;
    private EventBus mBus = EventBus.getDefault();
    private OnRetrievingDataListener mDataCallback;

    private User mCurrentUser;
    private List<User> mUsers;

    private TrailPresenter(DataModel model, CurrentTrailView view) {
        this.mModel = model;
        this.mView = view;
        mBus.register(this);
        this.setDatabaseRefs();
    }

    public boolean updateUserLocation(Location current) {
        if (current != null) {
            this.mCurrentUser.setCurrentLocation(current);
            this.mModel.updateCurrentUser(mCurrentUser);
            return true;
        }
        return false;
    }

    public void setCallBack(OnRetrievingDataListener callback)
    {
        if(callback!=null)
            mDataCallback=callback;
    }

    public void setDatabaseRefs() {
        mModel.setCurrentUserRef();
        mModel.setOtherUsersRef();
    }

    @Subscribe
    public void RetrieveCurrentUser(User user) {
        Log.i("user data",user.toString());
        if (user != null)
            mDataCallback.onCurrentUserRetrieved(user);
            mCurrentUser = user;
    }

    @Subscribe
    public void RetrieveOtherUsers(ArrayList<User> users) {
        if (!users.isEmpty())
            mDataCallback.onCurrentUsersRetrieved(users);
            mUsers = users;
    }

    @Subscribe
    public void RetrieveError(Error error) {
        if(error!=null)
            mDataCallback.onErrorOccurance(error);
    }

    public void AddNewTrail(String name, Trail trail){
        mCurrentUser.trails.put(name, trail);
        this.mModel.updateCurrentUser(mCurrentUser);
    }

    /**
     * Used to destroy the instance of the presenter
     */
    public void destroy(){
        this.uniqueInstance = null;
    }
}
