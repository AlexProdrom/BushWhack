package bw.bushwhack.domains.profile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import bw.bushwhack.R;
import bw.bushwhack.data.DataModel;
import bw.bushwhack.domains.profile.interfaces.ProfileTrailListListener;
import bw.bushwhack.data.models.Trail;
import bw.bushwhack.domains.profile.viewholders.TrailListViewHolder;

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
    RecyclerView mTrailRecyclerList;

    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter mTrailListAdapter;

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

    }

    private void setupTrailListAdapter(Query query){
        mTrailListAdapter = new FirebaseRecyclerAdapter<Trail, TrailListViewHolder>(Trail.class, R.layout.trail_row_layout, TrailListViewHolder.class, query ) {
            @Override
            protected void populateViewHolder(TrailListViewHolder viewHolder, Trail model, int position) {

                viewHolder.setTextViewName(model.getName());
                viewHolder.setTextViewDistance(model.getDistance());
                // detecting the key of the object
                String key = mTrailListAdapter.getRef(position).getKey().toString();
                viewHolder.setTrailKeyReference(key);
                viewHolder.setTrailModel(model);
                // TODO: add the progress logic to the trail
//                viewHolder.setProgressBarStatus(model.getProgress().intValue());
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_trail_list, container, false);
        ButterKnife.bind(this, view);

        // not to recreate view again...
        if (savedInstanceState == null) {

            // TODO: separate the query to the other class
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trails");
            this.setupTrailListAdapter(ref);
            this.mTrailRecyclerList.setAdapter(mTrailListAdapter);

            // set the layout manager to position the items in the recycler view
            this.mLinearLayoutManager = new LinearLayoutManager(getActivity());
            this.mTrailRecyclerList.setLayoutManager(mLinearLayoutManager);

//            // get the dummy trails
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    ArrayList<Trail> trails = Trail.createTrailList(20);
//                    // final to be accessible in the inner run
//                    final TrailListAdapter tla = new TrailListAdapter(getActivity(), trails);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mPlannedTrailsList.setAdapter(tla);
//                        }
//                    });
//                }
//            }).start();
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
