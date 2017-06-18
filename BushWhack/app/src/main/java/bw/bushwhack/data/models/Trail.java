package bw.bushwhack.data.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bw.bushwhack.data.enums.StatusEnum;


public class Trail {

    private String mTrailName;
    private Double mProgress;
    private Dates mDate;
    private StatusEnum mStatus;
    private double mTotalDistance;

    private List<Marker> mMarkerList;

    public Trail() {
    }

    public Trail(String name, Dates date, StatusEnum status, double totalDistance, List<Marker> markers) {
        this.mTrailName = name;
        this.mDate = date;
        this.mProgress = 0.0;
        this.mStatus = status;
        this.mTotalDistance = totalDistance;
        this.mMarkerList = markers;
    }

    public Dates getDate() {
        return mDate;
    }

    public void setName(String name) {
        this.mTrailName = name;
    }

    public void setDate(Dates Date) {
        this.mDate = Date;
    }

    @Exclude
    public StatusEnum getStatusVal() {
        return mStatus;
    }

    public int getStatus() {
        return mStatus.getValue();
    }

    public void setStatus(int status) {
        this.mStatus = StatusEnum.values()[status];
    }

    public double getTotalDistance() {
        return mTotalDistance;
    }

    public void setTotalDistance(double TotalDistance) {
        this.mTotalDistance = TotalDistance;
    }

    // trail model code for the recycler

    @Deprecated
    public Trail(String name, Double distance) {
        this.mTrailName = name;
        // change later
        this.mTotalDistance = distance;
        // random progress...
//        this.mProgress = (new Random()).nextDouble() * 100;
        this.mProgress = 0.0;
        //this.mMarkers = new HashMap<>();
        this.mMarkerList = new ArrayList<>();
    }

    public String getName() {
        return this.mTrailName;
    }

    public Double getDistance() {
        return this.mTotalDistance;
    }

    public Double getProgress() {
        return this.mProgress;
    }

    // for the dummy data:
    // to be replaced with the actual method to retrieve data from the Firebase stuff
    @Deprecated
    public static ArrayList<Trail> createTrailList(int numTrails) {
        ArrayList<Trail> trails = new ArrayList<Trail>();
        Random rnd = new Random();
//        for (int i = 0; i < numTrails; i++) {
        trails.add(new Trail("My Trail #1", (rnd.nextDouble()) * 20));
//        }
        return trails;
    }

    public void addMarker(Marker marker) {
        if (mMarkerList == null)
            mMarkerList = new ArrayList<>();
        this.mMarkerList.add(marker);
    }

    public List<Marker> getMarkers() {
        return this.mMarkerList;
    }
}
