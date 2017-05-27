package bw.bushwhack.models;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by prodromalex on 5/28/2017.
 */

//Fields and code i think would be suitable for an user
public class User {
    private String mName;
    private Date mBirthday;
    private Location mCurrentLocation;

    public HashMap<String, Trail> trails;

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.mCurrentLocation = currentLocation;
    }

    public User(String name,Date birthday) {
        this.mName=name;
        this.mBirthday=birthday;
        this.trails=new HashMap<String,Trail>();
    }
}
