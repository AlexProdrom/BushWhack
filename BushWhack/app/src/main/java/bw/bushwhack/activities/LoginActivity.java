package bw.bushwhack.activities;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import bw.bushwhack.R;
import bw.bushwhack.fragments.SignInFragment;
import bw.bushwhack.interfaces.OnAuthorizationScreenSwitchListener;

public class LoginActivity extends AppCompatActivity implements OnAuthorizationScreenSwitchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check if the fragment container is present
        if(findViewById(R.id.fragment_container) != null){
            // if there is a saved instance, we do not need to load the new stuff - what if we join together this activity and the feed activity further?
            if(savedInstanceState != null){
                return; // we do not apply any changes
            }
            // if everything is clear, we add the new fragment
            SignInFragment signInFragment = new SignInFragment();
            this.setFragment(signInFragment);
        }
    }

    protected void setFragment(android.support.v4.app.Fragment fr){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fr).commit();
    }

    // implement the interface between the fragments and the activity
    @Override
    public void onSwitchAuthFragment(android.support.v4.app.Fragment fragment) {
        this.setFragment(fragment);
    }
}
