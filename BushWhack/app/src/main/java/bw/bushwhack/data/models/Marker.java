package bw.bushwhack.data.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import bw.bushwhack.data.enums.MarkerTypeEnum;

/**
 * Created by prodromalex on 5/28/2017.
 */
@IgnoreExtraProperties
public class Marker {
    private Location mLocation;
    private MarkerTypeEnum mType;
    private String mName;
    private boolean mIsReached;

    public Marker() {
    }

    public Marker(Location location, String name) {
        this.mLocation = location;
        //this.mType = type;
        this.mName = name;
        this.mIsReached = false;
        this.mType= MarkerTypeEnum.INTERMEDIATE;
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

    public void setName(String mName) {
        this.mName = mName;
    }

    public boolean getIsReached() {
        return this.mIsReached;
    }

    public void setIsReached(boolean isReached) {
        this.mIsReached = isReached;
    }
}
