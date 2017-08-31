package bw.bushwhack.domains.profile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.OnClick;
import bw.bushwhack.R;
import bw.bushwhack.data.models.Location;
import bw.bushwhack.domains.trails.activeview.TrailActivity;
import bw.bushwhack.domains.trails.creating.MapsActivity;
import bw.bushwhack.domains.authorization.LoginActivity;
import bw.bushwhack.domains.profile.fragments.ProfileInfoFragment;
import bw.bushwhack.domains.profile.fragments.ProfileTrailListFragment;
import bw.bushwhack.global.events.Error;
import bw.bushwhack.global.interfaces.OnRetrievingDataListener;
import bw.bushwhack.domains.profile.interfaces.ProfileHeaderListener;
import bw.bushwhack.domains.profile.interfaces.ProfileTrailListListener;
import bw.bushwhack.data.models.User;
import bw.bushwhack.domains.trails.TrailPresenter;
import bw.bushwhack.global.interfaces.Presenter;
import bw.bushwhack.global.utils.LocationUtil;

public class ProfileActivity extends AppCompatActivity implements
        ProfileHeaderListener, ProfileTrailListListener, OnRetrievingDataListener {

    @BindView(R.id.profile_bottom_navigation)
    BottomNavigationView mBottomNavigation;


    ImageView mProfileImage;

    private int PERMSISSION_REQUEST_CODE = 123;
    private static final int GALLERY_INTENT=1;

    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    private Fragment mFragmentProfileTrails;
    // think about the naming...
    private ProfilePresenter mPresenter;
    private ProfileInfoFragment mProfileTab;
    private boolean mPausedByPermissionRequest;

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method
        if (!mPausedByPermissionRequest) {

            mPausedByPermissionRequest = true;
            LocationUtil.verifyLocationPermissions(this, this.PERMSISSION_REQUEST_CODE);
        }
    }
    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
        this.mPausedByPermissionRequest = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // create it if it was not yet
        mPresenter = ProfilePresenter.getInstance();
        mPresenter.setCallBack(this);


        ButterKnife.bind(this);
        final Context context = this;

        LocationUtil.verifyLocationPermissions(this, this.PERMSISSION_REQUEST_CODE);
        // bind navigation click events:
        mBottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_start_trail:
                                boolean hasCurrentTrail = ProfilePresenter.getInstance().attemptContinuingATrail();
                                if (hasCurrentTrail) {

                                    if (ProfilePresenter.getInstance().getGrantedPermissions()) {

                                        context.startActivity(new Intent(context, TrailActivity.class));
                                    } else {
                                        Toast.makeText(context, "Please grant location permissions first", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Huh, looks like you haven't start one yet ;)", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.action_new_trail:
                                if (ProfilePresenter.getInstance().getGrantedPermissions()) {

                                    onAddTrail();
                                } else {

                                    Toast.makeText(context, "Please grant location permissions first", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.action_settings:
                                Toast.makeText(context, "Bye-bye bushwhack!", Toast.LENGTH_SHORT).show();
//                                finish();
                                mFragmentProfileTrails.onDetach();
                                // to clear out the presenter reference and force reinitialization on new openning
                                mPresenter.destroy();
                                // to try removing the location listeners
                                TrailPresenter.getInstance().tryStopLocationListeneres();
                                finish();
                                mAuth.signOut();
                                startActivity(new Intent(context, LoginActivity.class));
                                break;
                        }
                        return false;
                    }
                }
        );

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        if (mAuth.getCurrentUser() == null) {
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        // set the profile header fragment:
        //Fragment frProfileTab = ProfileInfoFragment.newInstance(this.mPresenter.getmCurrentUser());
        mProfileTab = ProfileInfoFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_toolbar_fragment_frame, mProfileTab).commit();
        // set the profile trails
        mFragmentProfileTrails = new ProfileTrailListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_trail_list_fragment_frame, mFragmentProfileTrails).commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.PERMSISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    setLocationFunctionality(true);
                } else {

                    setLocationFunctionality(false);
                }
            } else {

                setLocationFunctionality(false);
            }
        }
    }

    private void setLocationFunctionality(boolean enabled) {
//        this.mBottomNavigation.getMenu().findItem(R.id.action_start_trail).setEnabled(enabled);
//        this.mBottomNavigation.getMenu().findItem(R.id.action_new_trail).setEnabled(enabled);
        ProfilePresenter.getInstance().setGrantedPermissions(enabled);
    }

    @Override
    public void onAddTrail() {
        Toast.makeText(this, "I intent to create a new trail!", Toast.LENGTH_SHORT).show();
        // Start Maps Activity, make the different ways to start later since there will be different fragments for the maps
        Intent myIntent = new Intent(this,
                MapsActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onCurrentUserRetrieved(User u) {
        // works to set the profile name and email, but lags a bit
        Log.i("user in profile", u.toString());
        mProfileTab.setProfileName(u.getName());
        mProfileTab.setProfileEmail(u.getEmail());
        mProfileTab.setProfileImage();
    }

    @Override
    public void onCurrentUsersRetrieved(ArrayList<User> users) {

        Log.i("user in profile", users.toString());
    }

    @Override
    public void onErrorOccurance(Error error) {

        Log.i("user in profile", error.toString());
    }

    //Image upload try-out
    //Permission for API>22
    public void onUploadImage(View v) {
        Log.i("upload", "upload image!!!");

        mPresenter.prepareImage();

        Intent picker=new Intent(Intent.ACTION_PICK);
        picker.setType("image/*");
        startActivityForResult(picker,GALLERY_INTENT);


        /*
        --Too capture image
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();

            StorageReference filepath=mStorage.child("photos").child(mAuth.getCurrentUser().getUid());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    @SuppressWarnings("VisibleForTests") Uri downloadUri=taskSnapshot.getDownloadUrl();
                    mProfileTab.setProfileImage();
                    Toast.makeText(ProfileActivity.this,mProfileImage.toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
        /*
        --Too upload captured image
        if(requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK)
        {
        progressbar.setMessage("Uploading image");
        progressbar.show();

            Uri uri=data.getData();
            StorageReference filepath=mStorage.child("photos").child(mAuth.getCurrentUser().getUid());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                progress.dismiss();
                    Toast.makeText(ProfileActivity.this,"Upload Done",Toast.LENGTH_LONG).show();
                }
            });
         */
    }
}
