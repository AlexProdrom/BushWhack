package bw.bushwhack.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by prodromalex on 5/28/2017.
 */
@IgnoreExtraProperties
public class Location {
    private double lat;
    private double lng;

    public Location() {}

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}