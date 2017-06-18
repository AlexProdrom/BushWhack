package bw.bushwhack.domains.trails.creating.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bw.bushwhack.R;
import bw.bushwhack.data.models.Marker;
import bw.bushwhack.data.models.Trail;
import bw.bushwhack.domains.trails.creating.MapsActivity;
import bw.bushwhack.domains.trails.creating.helpers.RecyclerListAdapter;
import bw.bushwhack.domains.trails.creating.helpers.SimpleItemTouchHelperCallback;


public class ReorderMarkersFragment extends DialogFragment {

    private ItemTouchHelper mItemTouchHelper;
  public static List<Marker> mMarkers;

    public List<Marker> getmMarkers() {
        return mMarkers;
    }

    public void setmMarkers(List<Marker> mMarkers) {
        this.mMarkers = mMarkers;
    }



    public ReorderMarkersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reorder_markers, container, false);
        getDialog().setTitle("Added markers");

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mMarkers!=null) {
            RecyclerListAdapter adapter = new RecyclerListAdapter(mMarkers);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_markers);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }


}
