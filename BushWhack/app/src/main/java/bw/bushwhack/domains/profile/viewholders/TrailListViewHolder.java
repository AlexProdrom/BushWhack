package bw.bushwhack.domains.profile.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bw.bushwhack.R;
import bw.bushwhack.data.models.Trail;
import bw.bushwhack.data.models.User;
import bw.bushwhack.domains.profile.ProfilePresenter;
import bw.bushwhack.domains.trails.activeview.TrailActivity;

/**
 * Created by Dmitry on 6/15/2017.
 */

public class TrailListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.text_view_trail_name)
    TextView mTrailName;
    @BindView(R.id.text_view_trail_distance)
    TextView mTrailDistance;
    @BindView(R.id.progress_bar_trail_status)
    ProgressBar mProgressBar;
    @BindView(R.id.button_trail_action)
    ImageButton mButtonMoreAction;
    @BindView(R.id.button_trail_selected)
    ImageButton mButtonSelectedTrail;


    private String mTrailReferenceKey;
    private Trail mTrailModel;

    public TrailListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mButtonMoreAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Toast.makeText(v.getContext(), "Pressed the button", Toast.LENGTH_SHORT).show();
        // register the button click event
        //Creating the instance of PopupMenu
        final Context context = v.getContext();
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.trail_row_popup_menu, popup.getMenu());
        final View viewAnchor = v;
        final TrailListViewHolder self = this;
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String menuOption = item.getTitle().toString();

//                Toast.makeText(context, "You are interested in trail " + mTrailReferenceKey,Toast.LENGTH_SHORT).show();
//                return false;

                switch (menuOption) {
                    case "Start":
                        if (ProfilePresenter.getInstance().getGrantedPermissions()) {

                            ProfilePresenter.getInstance().setNewCurrentTrail(mTrailReferenceKey);
                            context.startActivity(new Intent(context, TrailActivity.class));
                        }else{
                            Toast.makeText(context,"Please grant location permissions first",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Review":
                        self.displayTrailInfo(viewAnchor);
                        break;
                    case "Remove":

                        if(!ProfilePresenter.getInstance().removeTrail(mTrailReferenceKey)){
                            Toast.makeText(context,"Do not delete your current trails... :C", Toast.LENGTH_SHORT).show();
                        };
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

    public void displayTrailInfo(View v) {
        String text = "";
        if (this.mTrailModel != null) {

            Date date = this.mTrailModel.getDate().getStartDate();
            String dateString = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm", Locale.ENGLISH).format(date);
//            String timeString = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date);
            text = "Created " + dateString;
            // where to link

        } else {

            text = "No extra information available";
        }
        // TODO: maybe we can make the snackbar work somehow?
//        View viewAnchor = ((Activity) v.getContext()).findViewById(R.id.activity_profile);
        Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public String getTrailViewNameString() {
        return mTrailName.getText().toString();
    }

    public String getTrailViewDistanceString() {
        return mTrailDistance.getText().toString();
    }

    public int getProgressValue() {
        return this.mProgressBar.getProgress();
    }


    public void setTextViewName(String name) {
        this.mTrailName.setText(name);
    }

    public void setTextViewDistance(Double distance) {
        String dist = String.format("%.1f", distance);
        this.mTrailDistance.setText("Distance: " + dist + " km");
    }

    public void setSelectionIndicator(String key) {
        User user = ProfilePresenter.getInstance().getCurrentUser();
        if (user.getCurrentTrail() != null) {
            if (user.getCurrentTrail().equals(key)) {
                mButtonSelectedTrail.setVisibility(View.VISIBLE);
            } else {
                mButtonSelectedTrail.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.button_trail_selected)
    public void showSelected() {
        Context context = this.mButtonSelectedTrail.getContext();
        Toast.makeText(context, "This is your selected trail!", Toast.LENGTH_SHORT).show();
    }

    public void setProgressBarStatus(int progress) {
        this.mProgressBar.setProgress(progress);
    }

    public void setTrailKeyReference(String key) {
        this.mTrailReferenceKey = key;
    }

    public void setTrailModel(Trail model) {
        mTrailModel = model;
    }
}
