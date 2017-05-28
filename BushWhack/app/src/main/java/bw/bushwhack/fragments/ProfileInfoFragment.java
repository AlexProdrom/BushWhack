package bw.bushwhack.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import bw.bushwhack.R;
import bw.bushwhack.interfaces.ProfileHeaderListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileHeaderListener} interface
 * to handle interaction events.
 * Use the {@link ProfileInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileInfoFragment extends android.support.v4.app.Fragment {


    // TODO: add button interaction and more attributes
    // TODO: make actual interface subscription with the activity
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @BindView(R.id.text_nickname)
    TextView nickname;

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    private ProfileHeaderListener mListener;

    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileInfoFragment newInstance() {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        // add some logic
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for testing purposes -> to display one or another picture on start
        Random rnd = new Random();
        // the random picture flag
        boolean pictureRnd = rnd.nextBoolean();
        try {
            if (pictureRnd) {
                // displays picture a
                this.profile_image.setImageBitmap(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.picture_dummy_a));
            } else {
                // displays picture b
                this.profile_image.setImageBitmap(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.picture_dummy_b));
            }
        }catch (Exception e){
            System.out.println("Having problems getting the bitmap: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile_info, container, false);
        //Butterknife configuration
        ButterKnife.bind(this, v);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        return v;
    }

    // doesn't need to be here actually yet
    //    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileHeaderListener) {
            mListener = (ProfileHeaderListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement the profile header listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
