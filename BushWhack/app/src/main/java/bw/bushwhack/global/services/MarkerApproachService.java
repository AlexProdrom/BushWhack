package bw.bushwhack.global.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import android.location.LocationListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bw.bushwhack.R;
import bw.bushwhack.domains.trails.activeview.TrailActivity;
import bw.bushwhack.data.models.User;

import static android.app.Notification.DEFAULT_ALL;

//TODO:check if it works by only adding notification in the TrailActivity, otherwise update this service
public class MarkerApproachService extends Service {

    private LocationManager mLocationManager;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private LocationListener mLocationListenerGPS = new MyLocationListener(LocationManager.GPS_PROVIDER);

    private static final String TAG = "GPS";
    private static final int LOCATION_INTERVAL = 1000;
    private static final int LOCATION_DISTANCE = 100;
    private static final int NOTIFICATION_ID = 1;

    private final List<User> peopleList = new ArrayList<>();
    private User mCurrentUser;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
        mLocationManager.removeUpdates(mLocationListenerGPS);
    }

    @Override
    public void onCreate() {
        initialize();
        setOtherUsersRef();
        mUser = mAuth.getCurrentUser();

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL,
                    LOCATION_DISTANCE, mLocationListenerGPS);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        }
    }

    private void initialize() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
        }
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
    }

    public void setOtherUsersRef() {
        DatabaseReference ref = mDatabase.getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!postSnapshot.getKey().equals(mUser.getUid())) {
                        User person = postSnapshot.getValue(User.class);
                        peopleList.add(person);
                    } else {
                        mCurrentUser = postSnapshot.getValue(User.class);
                        distanceChecks();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void distanceChecks() {
        createNotification();
    }

    private void createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent trailIntent = new Intent(getApplicationContext(), TrailActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), trailIntent, 0);

        Notification n = new Notification.Builder(getApplicationContext())
                .setContentTitle("BushWhack")
                .setContentText("Approaching marker!")
                .setSmallIcon(R.drawable.ic_marker_notification)
                .setContentIntent(pIntent)
                .setDefaults(DEFAULT_ALL)
                .build();

        notificationManager.notify(NOTIFICATION_ID, n);
    }

    private class MyLocationListener implements LocationListener {

        private Location mLastLocation;

        private MyLocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        public Location getLastLocation() {
            return mLastLocation;
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Location connected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
