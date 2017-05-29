package bw.bushwhack.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

	// trail model code for the recycler
	// TODO: clean up the code and integrate
	private String mTrailName;
    private Double mProgress;

    public Trail(String name, Double distance){
        this.mTrailName = name;
		// change later
        this.mTotalDistance = distance;
        // random progress...
        this.mProgress = (new Random()).nextDouble()*100;
    }

    public String getName(){
        return this.mTrailName;
    }

    public Double getDistance(){
        return this.mTotalDistance;
    }

    public Double getmProgress(){
        return this.mProgress;
    }

    // for the dummy data:
    // to be replaced with the actual method to retrieve data from the Firebase stuff
    public static ArrayList<Trail> createTrailList(int numTrails){
        ArrayList<Trail> trails = new ArrayList<Trail>();
        Random rnd = new Random();
        for(int i =0; i<numTrails; i++){
            trails.add(new Trail("My Trail #"+i, (rnd.nextDouble())*20));
        }
        return trails;
    }

}
