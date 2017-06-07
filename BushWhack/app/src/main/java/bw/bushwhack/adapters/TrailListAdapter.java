package bw.bushwhack.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bw.bushwhack.R;
import bw.bushwhack.activities.TrailActivity;
import bw.bushwhack.models.Trail;

/**
 * Created by Dmitry on 5/26/2017.
 */

public class TrailListAdapter extends RecyclerView.Adapter<TrailListAdapter.ViewHolder> {

    // to inflate the item layout and create the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the row layout and return the instance of the holder
        View trailView = inflater.inflate(R.layout.trail_row_layout, parent, false);
        ViewHolder vh = new ViewHolder(trailView);
        return vh;
    }

    // set the view attributes based on the data
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // get the trail at the specified position
        Trail trail = mTrails.get(position);
        // assign the according attributes:
        TextView tvName = holder.mTrailName;
        tvName.setText(trail.getName());
        // the distance string should be uniform, but it'd be okay this way
        TextView tvDistance = holder.mTrailDistance;
        String dist = String.format("%.1f", trail.getDistance());
        Log.i("Distance format: ", dist);
        Log.i("Trail info", trail.getName());
        Log.i("Trail count", Integer.toString(mTrails.size()));
        tvDistance.setText("Distance: " + dist + " km");
        // since the progress is a Double we need to convert using intValue() on the object
        ProgressBar pbStatus = holder.mProgressBar;
        pbStatus.setProgress(trail.getmProgress().intValue());
    }

    // to determine the number of items
    @Override
    public int getItemCount() {
        return this.mTrails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_view_trail_name)
        TextView mTrailName;
        @BindView(R.id.text_view_trail_distance)
        TextView mTrailDistance;
        @BindView(R.id.progress_bar_trail_status)
        ProgressBar mProgressBar;
        @BindView(R.id.button_trail_action)
        ImageButton mButtonMoreAction;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mButtonMoreAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Pressed the button", Toast.LENGTH_SHORT).show();
            // register the button click event
            //Creating the instance of PopupMenu
            final Context context = v.getContext();
            PopupMenu popup = new PopupMenu(context, v);
            popup.getMenuInflater().inflate(R.menu.trail_row_popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    String menuOption = item.getTitle().toString();
                    switch (menuOption) {
                        case "Start":
                            context.startActivity(new Intent(context, TrailActivity.class));
                            break;
                        default:
                            Toast.makeText(context, "You chose for " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    private List<Trail> mTrails;
    private Context mContext;

    public TrailListAdapter(Context context, List<Trail> trails) {
        this.mContext = context;
        this.mTrails = trails;
    }

    private Context getContext() {
        return this.mContext;
    }
}
