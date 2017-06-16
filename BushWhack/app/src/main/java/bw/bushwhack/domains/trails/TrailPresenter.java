package bw.bushwhack.domains.trails;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import bw.bushwhack.global.interfaces.OnRetrievingDataListener;
import bw.bushwhack.data.DataModel;
import bw.bushwhack.data.models.Location;
import bw.bushwhack.data.models.Trail;
import bw.bushwhack.data.models.User;
import bw.bushwhack.global.interfaces.Presenter;
import bw.bushwhack.global.views.CurrentTrailView;

/**
 * Created by prodromalex on 6/3/2017.
 */

public class TrailPresenter implements Presenter {
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
            // replaced with updating only the location
//            this.mModel.updateCurrentUser(mCurrentUser);
            this.mModel.updateUserLocation(mCurrentUser.getCurrentLocation());
            return true;
        }
        return false;
    }

    @Override
    public void setCallBack(OnRetrievingDataListener callback)
    {
        if(callback!=null)
            mDataCallback=callback;
    }

    @Override
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

    @Override
    @Subscribe
    public void RetrieveError(Error error) {
        if(error!=null)
            mDataCallback.onErrorOccurance(error);
    }

    public void AddNewTrail(String name, Trail trail){
//        mCurrentUser.trails.put(name, trail);
        mCurrentUser.addTrail(trail);
        this.mModel.saveNewTrail(trail);
//        this.mModel.updateCurrentUser(mCurrentUser);
    }

    /**
     * Used to destroy the instance of the presenter
     */
    @Override
    public void destroy(){
        this.uniqueInstance = null;
    }
}