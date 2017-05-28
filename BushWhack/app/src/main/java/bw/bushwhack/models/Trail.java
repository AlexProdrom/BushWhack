package bw.bushwhack.models;

import java.util.HashMap;
import bw.bushwhack.enums.StatusEnum;

/**
 * Created by prodromalex on 5/28/2017.
 */

public class Trail {
    private Dates mDate;
    public HashMap<String,Marker> mMarkers;
    private int mStatus;
    private double mTotalDistance;

    public Trail(Dates date,StatusEnum status, double totalDistance) {
        this.mDate = date;
        this.mMarkers = new HashMap<>();
        this.mStatus = status.getValue();
        this.mTotalDistance = totalDistance;
    }

    public Dates getDate() {
        return mDate;
    }

    public void setDate(Dates Date) {
        this.mDate = Date;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(StatusEnum Status) {
        this.mStatus = Status.getValue();
    }

    public double getTotalDistance() {
        return mTotalDistance;
    }

    public void setTotalDistance(double TotalDistance) {
        this.mTotalDistance = TotalDistance;
    }

}
