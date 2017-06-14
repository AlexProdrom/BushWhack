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
    private String mName;

    public Marker() {}

    public Marker(Location location, MarkerTypeEnum type, String name) {
        this.mLocation = location;
        this.mType = type;
        this.mName = name;
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

    public String getName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
