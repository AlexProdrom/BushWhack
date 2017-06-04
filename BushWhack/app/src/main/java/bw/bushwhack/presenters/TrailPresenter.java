package bw.bushwhack.presenters;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import bw.bushwhack.models.DataModel;
import bw.bushwhack.models.Location;
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

    private DataModel mModel;
    private CurrentTrailView mView;
    private EventBus mBus = EventBus.getDefault();

    private User mCurrentUser;
    private List<User> mUsers;

    private TrailPresenter(DataModel model, CurrentTrailView view) {
        this.mModel = model;
        this.mView = view;
        mBus.register(this);
        this.setDatabaseRefs();
    }

    public boolean updateUserLocation(Location current)
    {
        if(current!=null) {
            this.mCurrentUser.setCurrentLocation(current);
            this.mModel.updateCurrentUser(mCurrentUser);
            return true;
        }
        return false;
    }

    public void setDatabaseRefs()
    {
        //A better location for this methods needs to be found as further on you can check users without a map
        mModel.setCurrentUserRef();
        mModel.setOtherUsersRef();
    }
}