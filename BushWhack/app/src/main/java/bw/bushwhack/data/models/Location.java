package bw.bushwhack.data.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

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

    //Return the official Android/Google location type
    public android.location.Location getAndroidLocation()
    {
        android.location.Location l=new android.location.Location("");
        l.setLongitude(lng);
        l.setLatitude(lat);

        return l;
    }

    //Return the LatLng object created using the lat and lng from here
    public LatLng getLatLng()
    {
        return new LatLng(lat,lng);
    }
}
