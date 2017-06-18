package bw.bushwhack.domains.trails.activeview;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bw.bushwhack.R;
import bw.bushwhack.data.models.Marker;
import bw.bushwhack.domains.trails.activeview.interfaces.CurrentTrailCallback;
import bw.bushwhack.global.events.Error;
import bw.bushwhack.global.interfaces.OnRetrievingDataListener;
import bw.bushwhack.data.models.User;
import bw.bushwhack.domains.trails.TrailPresenter;
import bw.bushwhack.global.services.MarkerApproachService;

import static android.app.Notification.DEFAULT_ALL;

// TODO: the rqeuests for the location permissions ruin the application on every frist time start up -> we need to handle it somehow

public class TrailActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnRetrievingDataListener,
        CurrentTrailCallback,
        android.location.LocationListener {

    private final int LOCATION_UPDATE_TIME_INTERVAL = 500;
    private final int LOCATION_UPDATE_DISTANCE_INTERVAL = 3;
    private final int MIN_DISTANCE_TO_MARKER_FOR_NOTIFICATION = 100;

    private GoogleMap mTrailMap;
    private List<User> mUsers;
    private TrailPresenter mTrailPresenter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private Location mLastKnownLocation;
    private final int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkLocationPermission();
        // set the presenter integration
        this.mTrailPresenter = TrailPresenter.getInstance();
        mTrailPresenter.setCallBack(this);

        this.mUsers = new ArrayList<>();
        // prepare the view
        setContentView(R.layout.activity_trail);
        // Obtain the SMF and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.trail_map);
        mapFragment.getMapAsync(this);
        // binding of the map fragment done
    }

    //onStart and onStop in order to manage the notifications through service when user is away from the current trail

    /*
    @Override
    protected void onStart() {
        super.onStart();
        Intent markerApproachService = new Intent(this, MarkerApproachService.class);
        stopService(markerApproachService);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent markerApproachService = new Intent(this, MarkerApproachService.class);
        startService(markerApproachService);
    }
    */

    public void changeCameraLocation(LatLng latLng) {
        mTrailMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mTrailMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
    }


    // TODO: should be moved to a separate class or object I suppose
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    // TODO: figure ot why last known location is null on first call and what to do about it...

    /**
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mTrailMap = googleMap;

        // add the listener of the onMyLocationButtonClick()
        mTrailMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                boolean permissionGranted = checkLocationPermission();
                // maybe there will be too many unnecessary checks
                if (permissionGranted) {
                    getDeviceLocation();
                }
                return permissionGranted;
            }
        });

        mTrailMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //initialize google play services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mTrailMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mTrailMap.setMyLocationEnabled(true);
        }

        //  query to check if permissions are enabled
        this.checkLocationPermission();
        this.getDeviceLocation();

//        if (this.mTrailPresenter.getUsers() != null) {
//            this.onCurrentUsersRetrieved((ArrayList<User>) this.mTrailPresenter.getUsers());
//        }
    }

    private void getDeviceLocation() {
    /*
     * Before getting the device location, you must check location
     * permission, as described earlier in the tutorial. Then:
     * Get the best and most recent location of the device, which may be
     * null in rare cases when a location is not available.
     */
        if (this.checkLocationPermission()) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mLastKnownLocation == null) {

            Log.d("No current location", "Current location is null. Using defaults.");
            if (this.isGpsLocationProviderEnabled()) {
                Location loc = this.mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mLastKnownLocation = loc;

            }
            if (mLastKnownLocation == null) {
                Log.d("Location", "Got location from the firebase");
                this.mLastKnownLocation = new Location("");
                this.mLastKnownLocation.setLatitude(this.mTrailPresenter.getCurrentUser().getCurrentLocation().getLat());
                this.mLastKnownLocation.setLongitude(this.mTrailPresenter.getCurrentUser().getCurrentLocation().getLng());
            }
//            couldn't get location - proposed solution
//            mTrailMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//            mTrailMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        try {
            mTrailMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//            // UPDATE ONLINE LOCATION
//            mTrailPresenter.updateUserLocation(
//                    new bw.bushwhack.data.models.Location(
//                            mLastKnownLocation.getLatitude(),
//                            mLastKnownLocation.getLongitude()
//                    )
//            );
        } catch (NullPointerException e) {
            Log.e("LastKnownLocation fail", e.getMessage());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setFastestInterval(40000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


        Log.i("location changed", "set to new location" + location.toString());
        this.mLastKnownLocation = location;

        // Update online location
        mTrailPresenter.updateUserLocation(
                new bw.bushwhack.data.models.Location(
                        location.getLatitude(),
                        location.getLongitude()
                )
        );
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        // changes the camera location
        // dismissed due to bad user experience
        //this.changeCameraLocation(latlng);

        // was a dumb mistake to put it here, ridicilous I haven't removed it after
        // now all the updates come from the location services
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }

        // clear the map
        this.mTrailMap.clear();
        // change and redrawthe user markers
        if (this.mTrailPresenter.getUsers() != null) {
            this.mUsers = this.mTrailPresenter.getUsers();
            this.displayUsersMarkers((ArrayList<User>) this.mUsers);
        }
        // don't forget to show the trail markers
        if (this.mTrailPresenter.getMarkers() != null) {
            this.displayTrailMarkers(mTrailPresenter.getMarkers());
        }

        List<Marker> markers = mTrailPresenter.getMarkers();

        if (markers != null) {
            for (int i = 0; i < markers.size(); i++) {
                Marker m = markers.get(i);
                Location markerLocation = m.getLocation().getAndroidLocation();
                int distanceToMarker = (int) Math.floor(location.distanceTo(markerLocation));
                if (distanceToMarker < MIN_DISTANCE_TO_MARKER_FOR_NOTIFICATION && !m.getIsReached()) {

                    // locally
                    m.setIsReached(true);
                    createNotification(markers.get(i).getName(), distanceToMarker);
                    // write to the database
                    mTrailPresenter.setMarkerReached(i);
                    Log.d("MarkerReached", "Reached the marker with id: " + i + " and name " + markers.get(i).getName());
                }

            }
        }
    }

    private void createNotification(String markerName, double distance) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent trailIntent = new Intent(getApplicationContext(), TrailActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), trailIntent, 0);

        Notification n = new Notification.Builder(getApplicationContext())
                .setContentTitle("BushWhack")
                .setContentText("Approaching marker " + markerName + " in " + distance + "m !")
                .setSmallIcon(R.drawable.ic_marker_notification)
                .setContentIntent(pIntent)
                .setDefaults(DEFAULT_ALL)
                .build();

        notificationManager.notify(((int) System.currentTimeMillis()), n);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // the code from StackOverflow to check if the permission is fine and all that
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean isGpsLocationProviderEnabled() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        }
        boolean enabled = false;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // could call the permission request here, but we target API 21 + which can't handle it
            }
            enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_TIME_INTERVAL,
                    LOCATION_UPDATE_DISTANCE_INTERVAL,
                    this);
//            return enabled;
        } catch (Exception e) {
            Log.d("GPS broke", e.getMessage());
        }

        return enabled;
    }


    // I do not know what I am doing here
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        // should set the location to be enabled
                        mTrailMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onCurrentUserRetrieved(User u) {

    }

    // TODO: figure why it is not triggered every time on sttart up, but IS TRIGGERED ON EVERY current location button click
    @Override
    public void onCurrentUsersRetrieved(ArrayList<User> users) {
        // check for the correct types
        try {


            if (!users.isEmpty() && (users.get(0) instanceof User)) {
                mTrailMap.clear();
                this.mUsers = users;
//        ArrayList<MarkerOptions> markers = new ArrayList<>();
                // since the map was cleaned, display the markers again
                List<Marker> markerList = this.mTrailPresenter.getMarkers();
                if (markerList != null) {

                    this.displayTrailMarkers(markerList);
                }
                // show the list of the users
                this.displayUsersMarkers(users);
            }
        } catch (NullPointerException npe) {
            Log.e("ErrorUsers", npe.getMessage());
        }
    }

    protected void displayUsersMarkers(List<User> users) {
        // TODO: maybe add some logical filtering not to keep track of ALL the users
        if (!users.isEmpty()) {
            if (users.get(0) instanceof User) {
                for (User person : users) {
                    if (person.getCurrentLocation() != null) {

                        // add the check for the email being different
                        if (!Objects.equals(person.getEmail(), mTrailPresenter.getCurrentUser().getEmail())) {

                            // Toast.makeText(this, "Person has a location", Toast.LENGTH_SHORT).show();
                            Location personLoc = person.getCurrentLocation().getAndroidLocation();
                            LatLng personLocation = person.getCurrentLocation().getLatLng();

                            float totalDistance = personLoc.distanceTo(mLastKnownLocation);
                            Double kmDistance = Math.floor(totalDistance / 1000);
                            Double mDistance = Math.floor(totalDistance % 1000);

                            // TODO: find the way to display custom markers
//                            BitmapDescriptor icon = BitmapDescriptorFactory
//                                    .fromResource(R.drawable.ic_person_pin_circle_black_24dp);
                            if (kmDistance < 2) {

//                                float opacity = (48000/totalDistance);
//                                if(opacity>100){
//                                    opacity = 100;
//                                }
                                mTrailMap.addMarker(new MarkerOptions()
                                                .position(personLocation)
                                                .title(person.getName())
//                                                .alpha(opacity/100)
//                                    .icon(icon)
                                                .snippet("From you: "
                                                        +
                                                        kmDistance.intValue()
                                                        +
                                                        " km "
                                                        +
                                                        mDistance.intValue()
//                                            LocationUtil.CalculationByDistance(
//                                            new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLatitude())
//                                            , personLocation)
                                                        + " m")
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_black_24dp))
                                );
                            }
                        }
                    }
                }
            }
        }

    }


    @Override
    public void onErrorOccurance(Error error) {

        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void displayTrailMarkers(List<Marker> markerList) {
        for (Marker marker : markerList) {

            LatLng markerLocation = new LatLng(marker.getLocation().getLat()
                    , marker.getLocation().getLng());
            String markerName = marker.getName();

            // TODO: get the bitmap from the drawable
            // get the icon from the vector drawable
//                Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_golf_course_black_24dp, null);
//                Bitmap bitmapIcon = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
//                        vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//                BitmapDescriptor icon = BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_golf_course_black_24dp);

            // TODO: marker types...
            mTrailMap.addMarker(new MarkerOptions()
                    .position(markerLocation)
                    .title(markerName).zIndex(100)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            );
        }
    }

    @Override
    public void onRetrievedMarkers(List<Marker> markerList) {

        try {
            // this is bad...
            // clear the map
            mTrailMap.clear();
            // display the markers again...
            this.displayTrailMarkers(markerList);
            // since the map was cleaned, display the user markers again
            this.displayUsersMarkers(this.mUsers);

        } catch (NullPointerException npe) {

            Log.e("MarkerListNull", npe.getMessage());
        }
    }
}