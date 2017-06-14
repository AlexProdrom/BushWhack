package bw.bushwhack.activities;

import bw.bushwhack.R;
import bw.bushwhack.enums.MarkerTypeEnum;
import bw.bushwhack.enums.StatusEnum;
import bw.bushwhack.interfaces.OnRetrievingDataListener;
import bw.bushwhack.models.Dates;
import bw.bushwhack.models.Trail;
import bw.bushwhack.models.User;
import bw.bushwhack.presenters.TrailPresenter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnRetrievingDataListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TrailPresenter mTrailPresenter;
    private Trail mCurrentTrail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTrailPresenter = TrailPresenter.getInstance();
        mCurrentTrail = new Trail("Random Name",new Dates(new Date(), new Date()), StatusEnum.STARTED, 10);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName());
                final Place mPlace = place;

                changeCameraLocation(place.getLatLng());

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                mBuilder.setTitle("Do you want to save this place to your trail?");
                mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PrompUserWithMarker(mPlace.getLatLng());
                    }
                });

                mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LatLng latLng = mPlace.getLatLng();
                        changeCameraLocation(latLng);

                        MarkerOptions options = new MarkerOptions();
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        options.position(latLng);
                        options.title(mPlace.getAddress().toString());
                        mMap.addMarker(options);
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                // Log.i(TAG, "An error occurred: " + status);
            }
        });

        Button btnSaveTrail = (Button) findViewById(R.id.btn_savetrail);
        btnSaveTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mView = getLayoutInflater().inflate(R.layout.fragment_add_trail_name, null);
                final EditText mEdittextTrailName = (EditText) mView.findViewById(R.id.editTextTrailName);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                mBuilder.setTitle("Name your trail");
                mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mTrailPresenter.AddNewTrail(mEdittextTrailName.getText().toString(), mCurrentTrail);
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    public void changeCameraLocation(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //initialize google play services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                final LatLng mLatLng = latLng;
                PrompUserWithMarker(mLatLng);
            }
        });
    }


    public void PrompUserWithMarker(LatLng latLng){

        final LatLng mLatLng = latLng;

        //Get fragment for adding new marker
        View mView = getLayoutInflater().inflate(R.layout.fragment_new_marker, null);
        final EditText mMarkerName = (EditText) mView.findViewById(R.id.editTextMarkerName);
        final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinnerMarkerType);

        //Get arraylist from strings.xml and assign it to spinner for marker types
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(MapsActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.markerTypes));
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        //Create alert dialog
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
        mBuilder.setTitle("Fill in marker details");
        mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MarkerOptions options = new MarkerOptions();
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                options.position(mLatLng);
                options.title(mMarkerName.getText().toString() +" "+ mSpinner.getSelectedItem().toString());
                mMap.addMarker(options);

                bw.bushwhack.models.Marker marker = new bw.bushwhack.models.Marker(
                        new bw.bushwhack.models.Location(mLatLng.latitude, mLatLng.longitude),
                        MarkerTypeEnum.INTERMEDIATE,
                        mMarkerName.getText().toString());

                mCurrentTrail.addMarker(marker);
//                mCurrentTrail.mMarkers.put(
//                        mMarkerName.getText().toString(),
//                        new bw.bushwhack.models.Marker(
//                                new bw.bushwhack.models.Location(mLatLng.latitude, mLatLng.longitude),
//                            MarkerTypeEnum.INTERMEDIATE,
//                            mMarkerName.getText().toString())
//                        );


            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mTrailPresenter.updateUserLocation(new bw.bushwhack.models.Location(latlng.latitude, latlng.longitude));

        if(mGoogleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
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
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onCurrentUserRetrieved(User u) {

    }

    @Override
    public void onCurrentUsersRetrieved(ArrayList<User> users) {

    }

    @Override
    public void onErrorOccurance(Error error) {

    }
}
