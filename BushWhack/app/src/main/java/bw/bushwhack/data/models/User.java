package bw.bushwhack.data.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {

    private Location mCurrentLocation;
    private String mName;
    private String mEmail;
    private String currentTrail;
    private ArrayList<Trail> mTrails;

    public User() {
        this.mTrails = new ArrayList<>();
    }

    public User(String name, String email) {
        this.mName = name;
        this.mEmail = email;
        this.currentTrail = "";
        this.mTrails = new ArrayList<>();
    }

    public String getCurrentTrail() {
        return this.currentTrail;
    }

    public String setCurrentTrail(String key) {
        return this.currentTrail = key;
    }

    public String getEmail() {
        return this.mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

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
