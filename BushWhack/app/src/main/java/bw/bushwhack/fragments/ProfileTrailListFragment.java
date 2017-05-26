package bw.bushwhack.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import bw.bushwhack.R;
import bw.bushwhack.adapters.TrailListAdapter;
import bw.bushwhack.interfaces.ProfileTrailListListener;
import bw.bushwhack.models.Trail;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileTrailListListener} interface
 * to handle interaction even
 * Use the {@link ProfileTrailListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileTrailListFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.planned_trails_list)
    RecyclerView mPlannedTrailsList;

    private LinearLayoutManager mLinearLayoutManager;

    private ProfileTrailListListener mListener;

    public ProfileTrailListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileTrailListFragment.
     */
    public static ProfileTrailListFragment newInstance() {
        ProfileTrailListFragment fragment = new ProfileTrailListFragment();
        // Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

//        // get the dummy trails
//        ArrayList<Trail> trails = Trail.createTrailList(1);
//        TrailListAdapter tla = new TrailListAdapter(getActivity(), trails);
//        this.mPlannedTrailsList.setAdapter(tla);
//
//        // set the layout manager to position the items in the recycler view
//        this.mLinearLayoutManager = new LinearLayoutManager(getActivity());
//        this.mPlannedTrailsList.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_trail_list, container, false);
        ButterKnife.bind(this, view);

        // not to recreate view again...
        if (savedInstanceState == null) {

            // set the layout manager to position the items in the recycler view
            this.mLinearLayoutManager = new LinearLayoutManager(getActivity());
            this.mPlannedTrailsList.setLayoutManager(mLinearLayoutManager);


            // get the dummy trails
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Trail> trails = Trail.createTrailList(20);
                    // final to be accessible in the inner run
                    final TrailListAdapter tla = new TrailListAdapter(getActivity(), trails);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPlannedTrailsList.setAdapter(tla);
                        }
                    });
                }
            }).start();
        }

        return view;

    }

//    @OnClick(R.id.button_add_trail)
//    public void onNewTrail() {
//        if (mListener != null) {
//            mListener.onAddTrail();
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileTrailListListener) {
            mListener = (ProfileTrailListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProfileTrailListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
