package bw.bushwhack.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by prodromalex on 5/28/2017.
 */

@IgnoreExtraProperties
public class User {
    private Location mCurrentLocation;
    public HashMap<String, Trail> trails;
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
        this.trails = new HashMap<>();
    }

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
}
