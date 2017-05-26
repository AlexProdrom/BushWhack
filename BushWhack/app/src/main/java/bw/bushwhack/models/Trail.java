package bw.bushwhack.models;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dmitry on 5/26/2017.
 */

public class Trail {
    private String mTrailName;
    private Double mDistance;
    private Double mProgress;

    public Trail(String name, Double distance){
        this.mTrailName = name;
        this.mDistance = distance;
        // random progress...
        this.mProgress = (new Random()).nextDouble()*100;
    }

    public String getName(){
        return this.mTrailName;
    }

    public Double getDistance(){
        return this.mDistance;
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
