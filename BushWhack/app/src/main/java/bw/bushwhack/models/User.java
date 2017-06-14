package bw.bushwhack.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
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
    @Deprecated
    public HashMap<String, Trail> trails;
    private ArrayList<Trail> mTrails;

    public User() {
        this.trails = new HashMap<>();
        this.mTrails = new ArrayList<Trail>();
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
        this.mTrails = new ArrayList<Trail>();
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

    public void addTrail(Trail t){
        // TODO: FIX...
        if(this.mTrails == null){
            this.mTrails = new ArrayList<>();
        }
        this.mTrails.add(t);
    }


}
