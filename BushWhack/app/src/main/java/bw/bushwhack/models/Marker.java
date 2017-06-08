package bw.bushwhack.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import bw.bushwhack.enums.MarkerTypeEnum;

/**
 * Created by prodromalex on 5/28/2017.
 */
@IgnoreExtraProperties
public class Marker {
    private Location mLocation;
    private MarkerTypeEnum mType;

    public Marker() {}

    public Marker(Location location, MarkerTypeEnum type) {
        this.mLocation = location;
        this.mType = type;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    @Exclude
    public MarkerTypeEnum getTypeVal() {
        return mType;
    }

    public int getType() {
        return mType.getValue();
    }

    public void setType(int type) {
        this.mType = MarkerTypeEnum.values()[type];
    }
}
