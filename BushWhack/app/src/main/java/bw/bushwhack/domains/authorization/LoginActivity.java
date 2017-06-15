package bw.bushwhack.domains.authorization;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import bw.bushwhack.R;
import bw.bushwhack.domains.profile.ProfileActivity;
import bw.bushwhack.domains.authorization.fragments.SignInFragment;
import bw.bushwhack.domains.authorization.interfaces.OnAuthorizationScreenSwitchListener;

public class LoginActivity extends AppCompatActivity implements OnAuthorizationScreenSwitchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null => already logged in
        if(mAuth.getCurrentUser() != null){
            finish();

            //open profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

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

    protected void setFragment(Fragment fr){
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fr).commit();
    }

    // implement the interface between the fragments and the activity
    @Override
    public void onSwitchAuthFragment(Fragment fragment) {
        this.setFragment(fragment);
    }
}
