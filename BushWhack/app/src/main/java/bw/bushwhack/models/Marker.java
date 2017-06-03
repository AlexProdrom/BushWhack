package bw.bushwhack.models;

import com.google.firebase.database.IgnoreExtraProperties;

import bw.bushwhack.enums.MarkerTypeEnum;

/**
 * Created by prodromalex on 5/28/2017.
 */
@IgnoreExtraProperties
public class Marker {
    private Location mLocation;
    private int mType;

    public Marker(){}

    public Marker(Location location, MarkerTypeEnum type) {
        this.mLocation = location;
        this.mType = type.getValue();
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public int getType() {
        return mType;
    }

    public void setType(MarkerTypeEnum type) {
        this.mType = type.getValue();
    }
}
