package bw.bushwhack.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.OnClick;
import bw.bushwhack.R;
import bw.bushwhack.activities.LoginActivity;
import bw.bushwhack.activities.ProfileActivity;
import bw.bushwhack.interfaces.OnAuthorizationScreenSwitchListener;

import static com.google.android.gms.internal.zzt.TAG;

// TODO: add the actual authorization functionality

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAuthorizationScreenSwitchListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends android.support.v4.app.Fragment {

    //Firebase authentification
    private FirebaseAuth mAuth;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "email";

    //Inputs in order to create account
    @BindView(R.id.input_name) TextView name;
    @BindView(R.id.input_email) TextView email;
    @BindView(R.id.input_password) TextView password;

    private OnAuthorizationScreenSwitchListener listenerContext;

    public SignUpFragment() {
        // Required empty public constructor
    }

    // method to switch to the SignIn screen
    @OnClick(R.id.link_login)
    public void onGoToSignIn() {
        if (listenerContext != null) {
            SignInFragment signInFragment = SignInFragment.newInstance(this.email.getText().toString());
            listenerContext.onSwitchAuthFragment(signInFragment);
        }
    }

    //create a new account
    @OnClick(R.id.btn_signup)
    public void onSignUp() {
        //create user
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UpdateUi();
                        }
                    }

                });

    }

    void UpdateUi()
    {
        Intent mSwitch=new Intent(getActivity(),ProfileActivity.class);
        startActivity(mSwitch);
        getActivity().finish();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment SignUpFragment.
     */
    public static SignUpFragment newInstance(String email) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();

        if (getArguments() != null) {
            email.setText(getArguments().getString(ARG_PARAM1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAuthorizationScreenSwitchListener) {
            listenerContext = (OnAuthorizationScreenSwitchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAuthorizationScreenSwitchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listenerContext = null;
    }
}
