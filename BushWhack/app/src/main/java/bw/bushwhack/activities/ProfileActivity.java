package bw.bushwhack.activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bw.bushwhack.R;
import bw.bushwhack.fragments.ProfileInfoFragment;
import bw.bushwhack.interfaces.ProfileHeaderListener;

public class ProfileActivity extends AppCompatActivity implements ProfileHeaderListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // set the profile header fragment:
        Fragment fr = new ProfileInfoFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_toolbar_fragment_frame, fr).commit();

    }
}
