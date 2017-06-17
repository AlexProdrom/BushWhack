package bw.bushwhack.domains.trails;

import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import bw.bushwhack.data.models.Marker;
import bw.bushwhack.domains.trails.activeview.interfaces.CurrentTrailCallback;
import bw.bushwhack.domains.trails.creating.MapsActivity;
import bw.bushwhack.global.events.Error;
import bw.bushwhack.global.interfaces.OnRetrievingDataListener;
import bw.bushwhack.data.DataModel;
import bw.bushwhack.data.models.Location;
import bw.bushwhack.data.models.Trail;
import bw.bushwhack.data.models.User;
import bw.bushwhack.global.interfaces.Presenter;

/**
 * Created by prodromalex on 6/3/2017.
 */

public class TrailPresenter implements Presenter {
    private volatile static TrailPresenter uniqueInstance;

    public static TrailPresenter getInstance() {
        if (uniqueInstance == null) {
            synchronized (TrailPresenter.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TrailPresenter(new DataModel());
                }
            }
        }
        return uniqueInstance;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public List<Marker> getMarkers(){return mMarkers;}

    private DataModel mModel;
    private EventBus mBus = EventBus.getDefault();
    private OnRetrievingDataListener mDataCallback;

    private User mCurrentUser;
    private List<User> mUsers;
    private List<Marker> mMarkers;

    private TrailPresenter(DataModel model) {
        this.mModel = model;
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
    public void setCallBack(OnRetrievingDataListener callback) {
        if (callback != null)
            mDataCallback = callback;
        // only if the instance of the connected callback is the current trail viewer, set the refs
        if (mDataCallback instanceof CurrentTrailCallback) {
            mModel.setOtherUsersRef();
        }
    }

    @Override
    public void setDatabaseRefs() {
        mModel.setCurrentUserRef();
        // removed setting other user ref as we don't need it while in the creating trail activity
    }

    // for some reasons it is being called a lot of times
    @Subscribe
    public void RetrieveCurrentUser(User user) {
        Log.i("user data", user.toString());
        if (user != null)
            mDataCallback.onCurrentUserRetrieved(user);
        mCurrentUser = user;
        if(this.mDataCallback instanceof CurrentTrailCallback){
            this.retrieveCurrentTrailMarkers();
        }
    }

    @Subscribe
    public void RetrieveOtherUsers(ArrayList<User> users) {
        if (!users.isEmpty())
            mDataCallback.onCurrentUsersRetrieved(users);
        mUsers = users;
    }

    @Subscribe
    public void RetrieveError(Error error) {
        if (error != null)
            mDataCallback.onErrorOccurance(error);
    }

    public boolean AddNewTrail(Trail trail) {

       try{
        mCurrentUser.addTrail(trail);
        this.mModel.saveNewTrail(trail);
           System.out.println("Trail created");
           return true;
       }
       catch (Exception e){
           System.out.println(e.toString());
           return false;
       }


    }

    // for the current trail

    // set reference
    public void retrieveCurrentTrailMarkers() {
        mModel.setTrailMarkersRef(this.mCurrentUser.getCurrentTrail());
    }

    // get get the notification with the response
    // is subscribed to the eventbus notification
    @Subscribe
    public void onRetrieveCurrentTrailMarkers(List<Marker> markerList) {
        try {

            if (!markerList.isEmpty() && (markerList.get(0) instanceof Marker)) {
                this.mMarkers=markerList;
                ((CurrentTrailCallback) this.mDataCallback).onRetrievedMarkers(markerList);
            }
        } catch (Exception e) {
            Log.e("EventBusForMarkerList", e.getMessage());
        }
    }

    public void setMarkerReached(int markerNumber){
        mModel.setMarkerReached(this.mCurrentUser.getCurrentTrail(),markerNumber);
    }


    /**
     * Used to destroy the instance of the presenter
     */
    @Override
    public void destroy() {
        this.uniqueInstance = null;
    }
}
