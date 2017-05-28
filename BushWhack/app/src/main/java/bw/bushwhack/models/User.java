package bw.bushwhack.models;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by prodromalex on 5/28/2017.
 */

//Fields and code i think would be suitable for an user
public class User {
    private Location mCurrentLocation;
    public HashMap<String, Trail> trails;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.mCurrentLocation = currentLocation;
    }

    public User(String name) {
        this.name=name;
        this.trails=new HashMap<>();
    }
}
