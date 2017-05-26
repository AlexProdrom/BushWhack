package bw.bushwhack.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import bw.bushwhack.R;
import bw.bushwhack.fragments.ProfileInfoFragment;
import bw.bushwhack.fragments.ProfileTrailListFragment;
import bw.bushwhack.interfaces.ProfileHeaderListener;
import bw.bushwhack.interfaces.ProfileTrailListListener;

public class ProfileActivity extends AppCompatActivity implements ProfileHeaderListener, ProfileTrailListListener {

    @BindView(R.id.profile_bottom_navigation)
    BottomNavigationView mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        final Context context = this;
        // bind navigation click events:
        mBottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            // TODO: make the logical flow of the activities and fragments more manageable
                            case R.id.action_profile:
                                Toast.makeText(context,"Opens the profile fragment", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_new_trail:
                                onAddTrail();
                                break;
                            case R.id.action_settings:
                                Toast.makeText(context,"Opens the settings fragment", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                }
        );


        // set the profile header fragment:
        Fragment frProfileTab = new ProfileInfoFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_toolbar_fragment_frame, frProfileTab).commit();
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
}
