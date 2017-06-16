package bw.bushwhack.global.utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by Dmitry on 6/7/2017.
 * to be used for utility function related to the locations
 */

public class LocationUtil {

    // TODO: this does not really work, fix needed!
    // from https://stackoverflow.com/questions/14394366/find-distance-between-two-points-on-map-using-google-map-api-v2
    /**
     * Finds the absolute distance between the two points
     * @param StartP
     * @param EndP
     * @return meters
     */
    static public double CalculationByDistance(LatLng StartP, LatLng EndP) {



        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
//        return meterInDec ;
    }


}