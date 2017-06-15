package bw.bushwhack.domains.profile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import bw.bushwhack.R;
import bw.bushwhack.domains.trails.creating.MapsActivity;
import bw.bushwhack.domains.authorization.LoginActivity;
import bw.bushwhack.domains.profile.fragments.ProfileInfoFragment;
import bw.bushwhack.domains.profile.fragments.ProfileTrailListFragment;
import bw.bushwhack.global.interfaces.OnRetrievingDataListener;
import bw.bushwhack.domains.profile.interfaces.ProfileHeaderListener;
import bw.bushwhack.domains.profile.interfaces.ProfileTrailListListener;
import bw.bushwhack.data.models.User;
import bw.bushwhack.domains.trails.TrailPresenter;

public class ProfileActivity extends AppCompatActivity implements
        ProfileHeaderListener, ProfileTrailListListener, OnRetrievingDataListener {

    @BindView(R.id.profile_bottom_navigation)
    BottomNavigationView mBottomNavigation;

    private FirebaseAuth mAuth;
    // think about the naming...
    private TrailPresenter mPresenter;
    private ProfileInfoFragment mProfileTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // create it if it was not yet
        mPresenter = TrailPresenter.getInstance();
        mPresenter.setCallBack(this);

        ButterKnife.bind(this);
        final Context context = this;
        // bind navigation click events:
        mBottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profile:
                                Toast.makeText(context,"Opens the profile fragment", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_new_trail:
                                onAddTrail();
                                break;
                            case R.id.action_settings:
                                Toast.makeText(context,"Bye-bye bushwhack!", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                // to clear out the presenter reference and force reinitialization on new openning
                                mPresenter.destroy();
                                startActivity(new Intent(context, LoginActivity.class));
                                break;
                        }
                        return false;
                    }
                }
        );

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        // set the profile header fragment:
        //Fragment frProfileTab = ProfileInfoFragment.newInstance(this.mPresenter.getmCurrentUser());
        mProfileTab = ProfileInfoFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_toolbar_fragment_frame, mProfileTab).commit();
        // set the profile trails
        Fragment frProfileTrails = new ProfileTrailListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_trail_list_fragment_frame, frProfileTrails).commit();
    }

    @Override
    public void onAddTrail() {
        Toast.makeText(this,"I intent to create a new trail!",Toast.LENGTH_SHORT).show();
        // Start Maps Activity, make the different ways to start later since there will be different fragments for the maps
        Intent myIntent = new Intent(this,
                MapsActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onCurrentUserRetrieved(User u) {
        // works to set the profile name and email, but lags a bit
        Log.i("user in profile",u.toString());
        mProfileTab.setProfileName(u.getName());
        mProfileTab.setProfileEmail(u.getEmail());
    }

    @Override
    public void onCurrentUsersRetrieved(ArrayList<User> users) {

        Log.i("user in profile",users.toString());
    }

    @Override
    public void onErrorOccurance(Error error) {

        Log.i("user in profile",error.toString());
    }
}
