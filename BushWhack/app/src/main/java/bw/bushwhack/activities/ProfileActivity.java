package bw.bushwhack.activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bw.bushwhack.R;
import bw.bushwhack.fragments.ProfileInfoFragment;
import bw.bushwhack.fragments.ProfileTrailListFragment;
import bw.bushwhack.interfaces.ProfileHeaderListener;
import bw.bushwhack.interfaces.ProfileTrailListListener;

public class ProfileActivity extends AppCompatActivity implements ProfileHeaderListener, ProfileTrailListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

    }
}
