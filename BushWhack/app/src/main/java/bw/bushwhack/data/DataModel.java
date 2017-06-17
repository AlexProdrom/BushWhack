package bw.bushwhack.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bw.bushwhack.data.models.Location;
import bw.bushwhack.data.models.Marker;
import bw.bushwhack.data.models.Trail;
import bw.bushwhack.data.models.User;
import bw.bushwhack.global.events.Error;

/**
 * Created by prodromalex on 6/3/2017.
 */

public class DataModel {
    private EventBus mBus = EventBus.getDefault();
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;

    public DataModel() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void setCurrentUserRef() {
        DatabaseReference ref = mDatabase.getReference().child("users").child(mUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User person = dataSnapshot.getValue(User.class);
                mBus.post(person);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mBus.post(new Error(databaseError.getMessage()));
            }
        });
    }

    public void setOtherUsersRef() {
        DatabaseReference ref = FireBaseUtil.getInstance().getAllUsersReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<User> peopleList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User person = postSnapshot.getValue(User.class);
                    peopleList.add(person);
                }
                mBus.post(peopleList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mBus.post(new Error(databaseError.getMessage()));
            }
        });
    }

    public void saveNewTrail(Trail t) {
        if (t != null) {
            FireBaseUtil.getInstance()
                    .getCurrentUserTrailsReference()
                    .push()
                    .setValue(t);
//            for(Marker m : t.getMarkers()){
//                ref.child("markers").push().setValue(m);
//            }
        }
    }

    public void updateUserLocation(Location location) {
        DatabaseReference ref = FireBaseUtil.getInstance().getCurrentUserProfileReference().child("currentLocation");
        ref.setValue(location);
    }

    public void updateCurrentUser(User u) {
        if (u != null) {
            DatabaseReference ref = mDatabase.getReference().child("users").child(mUser.getUid());
            ref.setValue(u);
        }
    }

    public void setTrailMarkersRef(String trailKey){

        DatabaseReference ref = FireBaseUtil.getInstance()
               .getUserTrailMarkersReference(trailKey);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Marker> markerList = new ArrayList<Marker>();
                for(DataSnapshot markerSnapshot : dataSnapshot.getChildren()){
                    markerList.add(markerSnapshot.getValue(Marker.class));
                }
                mBus.post(markerList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mBus.post(new Error(databaseError.getMessage()));
            }
        });
    }
}
