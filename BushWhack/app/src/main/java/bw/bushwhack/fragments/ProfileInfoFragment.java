package bw.bushwhack.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import bw.bushwhack.R;
import bw.bushwhack.interfaces.ProfileHeaderListener;
import bw.bushwhack.models.User;
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


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mData;

    @BindView(R.id.text_nickname)
    TextView nickname;
    @BindView(R.id.text_name)
    TextView profile_name;
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
    public static ProfileInfoFragment newInstance() {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        // add some logic
        return fragment;
    }

    @Deprecated
    public static ProfileInfoFragment newInstance(User userData) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        // add some logic
        fragment.setProfileName(userData.getName());
        fragment.setProfileEmail(userData.getEmail());
        return fragment;
    }

    public void setProfileName(String name){
        this.profile_name.setText(name);
    }

    public void setProfileEmail(String email){
        this.nickname.setText(email);
    }

    // TODO: implement set ProfileImage
//    public void setProfileImage(){
//
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void displayRandomUser(){
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
            Log.e("Bitmap error: ", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // View view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        // ButterKnife.bind(this,view);
        // this.displayRandomUser();
        // return view;

        View v=inflater.inflate(R.layout.fragment_profile_info, container, false);
        //Butterknife configuration
        ButterKnife.bind(this, v);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        mData= FirebaseDatabase.getInstance();
        DatabaseReference ref1=mData.getReference(mUser.getUid()).child("name");

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
