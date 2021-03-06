package bw.bushwhack.domains.authorization.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bw.bushwhack.R;
import bw.bushwhack.domains.profile.ProfileActivity;
import bw.bushwhack.domains.authorization.interfaces.OnAuthorizationScreenSwitchListener;
import bw.bushwhack.data.models.User;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAuthorizationScreenSwitchListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    //Firebase authentication
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private OnAuthorizationScreenSwitchListener listenerContext;
    private ProgressDialog progressDialog;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "email";

    //Inputs in order to create account
    @BindView(R.id.input_name)
    TextView name;
    @BindView(R.id.input_email)
    TextView email;
    @BindView(R.id.input_password)
    TextView password;

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
        final String name_str=name.getText().toString();
        final String email_str=email.getText().toString().trim();
        String password_str=password.getText().toString().trim();

        //Checking for inputs
        if(TextUtils.isEmpty(email_str)){
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password_str)){
            Toast.makeText(getActivity(),"Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                            User userData=new User(name_str,email_str);
                            mRef.child("users").child(user.getUid()).setValue(userData);

                            startActivity(new Intent(getActivity(), ProfileActivity.class));
                        }
                        progressDialog.dismiss();
                    }

                });

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_sign_up, container, false);
        //Butterknife configuration
        ButterKnife.bind(this,v);

        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference();
        progressDialog=new ProgressDialog(getActivity());

        if (getArguments() != null) {
            email.setText(getArguments().getString(ARG_PARAM1));
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
                    + " must implement OnAuthorizationScreenSwitchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listenerContext = null;
    }
}
