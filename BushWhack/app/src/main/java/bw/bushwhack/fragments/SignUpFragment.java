package bw.bushwhack.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.OnClick;
import bw.bushwhack.R;
import bw.bushwhack.interfaces.OnAuthorizationScreenSwitchListener;

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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "email";

    private String email;

    private OnAuthorizationScreenSwitchListener listenerContext;

    public SignUpFragment() {
        // Required empty public constructor
    }

    // method to switch to the SignIn screen
    @OnClick(R.id.link_login)
    public void onGoToSignIn() {
        if (listenerContext != null) {
            SignInFragment signInFragment = SignInFragment.newInstance(this.email);
            listenerContext.onSwitchAuthFragment(signInFragment);
        }
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
//        // could be useful to pass the email between the fragments
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
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
