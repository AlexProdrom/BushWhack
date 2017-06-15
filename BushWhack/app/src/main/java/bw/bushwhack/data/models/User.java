package bw.bushwhack.data.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by prodromalex on 5/28/2017.
 */

@IgnoreExtraProperties
public class User {

    private Location mCurrentLocation;
    private String mName;
    private String mEmail;
    private String currentTrail;
    @Deprecated
    public HashMap<String, Trail> trails;
    private ArrayList<Trail> mTrails;

    // [Constructors]

    public User() {
        this.trails = new HashMap<>();
        this.mTrails = new ArrayList<Trail>();
    }
//
//    @Deprecated
//    public User(String name) {
//        this.name = name;
//        this.trails = new HashMap<>();
//    }

    public User(String name, String email) {
        this.mName = name;
        this.mEmail = email;
        this.currentTrail ="";
        this.trails = new HashMap<>();
        this.mTrails = new ArrayList<Trail>();
    }

    public String getCurrentTrail() {return this.currentTrail;}
    public String setCurrentTrail(String key){
        return this.currentTrail = key;
    }

    public String getEmail() {
        return this.mEmail;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setEmail(String email) { this.mEmail = email; }

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.mCurrentLocation = currentLocation;
    }

    public void addTrail(Trail t) {
        if (this.mTrails == null) {
            this.mTrails = new ArrayList<>();
        }
        this.mTrails.add(t);
    }


}
