package bw.bushwhack.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import bw.bushwhack.R;
import bw.bushwhack.interfaces.OnRetrievingDataListener;
import bw.bushwhack.models.User;
import bw.bushwhack.presenters.TrailPresenter;
import bw.bushwhack.utils.LocationUtil;

import static bw.bushwhack.activities.MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class TrailActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnRetrievingDataListener {

    private GoogleMap mTrailMap;
    private TrailPresenter mTrailPresenter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private Location mLastKnownLocation;
    private final int DEFAULT_ZOOM = 15;

    // TODO: move camera to the location on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the presenter integration
        this.mTrailPresenter = TrailPresenter.getInstance();
        mTrailPresenter.setCallBack(this);

        // prepare the view
        setContentView(R.layout.activity_trail);
        // Obtain the SMF and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.trail_map);
        mapFragment.getMapAsync(this);
        // binding of the map fragment done
    }

    public void changeCameraLocation(LatLng latLng) {
        mTrailMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mTrailMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
    }

    // TODO: could maybe be moved to a separate class/object?
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

                // TODO: this stuff triggers on users data retrieved, investiagate why
                boolean permissionGranted = checkLocationPermission();
                // maybe there will be too many unnecessary checks
                if (permissionGranted) {
                    getDeviceLocation();
                }
                return permissionGranted;
            }
        });

        // TODO: I remember the discussion whether we need to change it
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
        // TODO: explicitely move to the current location and write it to firebase
        if (this.mTrailPresenter.getUsers() != null) {
            this.onCurrentUsersRetrieved((ArrayList<User>) this.mTrailPresenter.getUsers());
        }
    }

    // TODO: fixed some stuff, but have to make sure the usage of the functions is not broken
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
//                    new bw.bushwhack.models.Location(
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
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
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

        // Update online location
        mTrailPresenter.updateUserLocation(
                new bw.bushwhack.models.Location(
                        location.getLatitude(),
                        location.getLongitude()
                )
        );
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        // changes the camera location
        this.changeCameraLocation(latlng);

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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
        try {
            return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Log.d("GPS broke", e.getMessage());
        }

        return false;
    }


    // I do not know what I am doing here
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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
        mTrailMap.clear();
//        ArrayList<MarkerOptions> markers = new ArrayList<>();
        // show the list of the users
        this.displayUsersMarkers(users);
    }

    protected void displayUsersMarkers(ArrayList<User> users) {
        for (User person : users) {
            if (person.getCurrentLocation() != null) {

                // add the check for the email being different
                if (person.getEmail() != mTrailPresenter.getCurrentUser().getEmail()) {

                    // Toast.makeText(this, "Person has a location", Toast.LENGTH_SHORT).show();
                    LatLng personLocation = new LatLng(person.getCurrentLocation().getLat(), person.getCurrentLocation().getLng());

                    Location personLoc = new Location("");
                    personLoc.setLongitude(personLocation.longitude);
                    personLoc.setLatitude(personLocation.latitude);

                    float totalDistance = personLoc.distanceTo(mLastKnownLocation);
                    Double kmDistance = Math.floor(totalDistance / 1000);
                    Double mDistance = Math.floor(totalDistance % 1000);

                    mTrailMap.addMarker(new MarkerOptions()
                                    .position(personLocation)
                                    .title(person.getName().toString())
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
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_black_24dp))
                    );
                }
            }
        }
    }


    @Override
    public void onErrorOccurance(Error error) {

        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}