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
    private String name;
    private  String email;
    public HashMap<String, Trail> trails;

    public User() {
        this.trails = new HashMap<>();
    }

    @Deprecated
    public User(String name) {
        this.name = name;
        this.trails = new HashMap<>();
    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.trails = new HashMap<>();
    }

    public String getEmail() {return  email;}

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
