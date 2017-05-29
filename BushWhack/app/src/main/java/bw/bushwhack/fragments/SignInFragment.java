package bw.bushwhack.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bw.bushwhack.R;
import bw.bushwhack.activities.ProfileActivity;
import bw.bushwhack.interfaces.OnAuthorizationScreenSwitchListener;

// TODO: add the actual authorization functionality

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAuthorizationScreenSwitchListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//
public class SignInFragment extends Fragment {

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private OnAuthorizationScreenSwitchListener listenerContext;

    @BindView(R.id.input_email)
    EditText email;

    @BindView(R.id.input_password)
    EditText password;


    public SignInFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment SignInFragment.
     */
    public static SignInFragment newInstance(String email) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(SignUpFragment.ARG_PARAM1, email);
        fragment.setArguments(args);
        return fragment;
    }


    @OnClick(R.id.btn_login)
    public void onSignIn() {
        String email_str = email.getText().toString().trim();
        String password_str = password.getText().toString().trim();

        //Checking for inputs
        if (TextUtils.isEmpty(email_str)) {
            Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password_str)) {
            Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email_str, password_str)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), ProfileActivity.class));
                        } else {
                            Toast.makeText(getActivity(), "Registration Failed!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    // method to switch to the SignUp screen
    @OnClick(R.id.link_signup)
    public void onGoToSignUp() {
        if (listenerContext != null) {
            SignUpFragment signUpFragment = SignUpFragment.newInstance(email.getText().toString());
            listenerContext.onSwitchAuthFragment(signUpFragment);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        //Butterknife configuration
        ButterKnife.bind(this, v);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        if (getArguments() != null) {
            email.setText(getArguments().getString(SignUpFragment.ARG_PARAM1));

        }
        return v;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnAuthorizationScreenSwitchListener) {
            listenerContext = (OnAuthorizationScreenSwitchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement the sign in interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listenerContext = null;
    }
}
