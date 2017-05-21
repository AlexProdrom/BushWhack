package bw.bushwhack.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import bw.bushwhack.R;
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
public class SignInFragment extends android.support.v4.app.Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "email";

    private String email;

    private OnAuthorizationScreenSwitchListener listenerContext;

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
        args.putString(ARG_PARAM1, email);
        fragment.setArguments(args);
        return fragment;
    }

    // method to switch to the SignIn screen
    // TODO: maybe would be worth making an interface for the auth screens?
    @OnClick(R.id.link_signup)
    public void onGoToSignUp() {
        if (listenerContext != null) {
            SignUpFragment signUpFragment = SignUpFragment.newInstance(this.email);
            listenerContext.onSwitchAuthFragment(signUpFragment);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // could be useful to pass the email between the fragments
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onAttach(Context context) {
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
