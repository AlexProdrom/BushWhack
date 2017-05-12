package bw.bushwhack.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import butterknife.BindView;
import bw.bushwhack.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileInfoFragment extends Fragment {


    // TODO: add button interaction and more attributes
    // TODO: make actual interface subscription with the activity

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    private OnFragmentInteractionListener mListener;

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
        boolean aorb = rnd.nextBoolean();
        if(aorb){
            this.profile_image.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.picture_dummy_a));
        }else{
            this.profile_image.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.picture_dummy_b));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_info, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
