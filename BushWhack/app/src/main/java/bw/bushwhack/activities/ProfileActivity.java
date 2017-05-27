package bw.bushwhack.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import bw.bushwhack.R;
import bw.bushwhack.fragments.ProfileInfoFragment;
import bw.bushwhack.interfaces.ProfileHeaderListener;

public class ProfileActivity extends AppCompatActivity implements ProfileHeaderListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            finish();

            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        // set the profile header fragment:
        Fragment fr = new ProfileInfoFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_toolbar_fragment_frame, fr).commit();

    }
}
