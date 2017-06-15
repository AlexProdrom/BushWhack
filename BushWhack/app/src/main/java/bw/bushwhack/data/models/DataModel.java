package bw.bushwhack.data.models;

import android.util.Log;

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
                Log.i("datasnap",dataSnapshot.toString());
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
        DatabaseReference ref = mDatabase.getReference().child("users");

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

    public void saveNewTrail(Trail t){
        if(t!=null){
            String uid = mUser.getUid();
            DatabaseReference ref = mDatabase.getReference().child("users").child(uid).child("trails").push();
            ref.setValue(t);
//            for(Marker m : t.getMarkers()){
//                ref.child("markers").push().setValue(m);
//            }
        }
    }

    public void updateUserLocation(Location location){
        DatabaseReference ref = mDatabase.getReference().child("users").child(mUser.getUid()).child("currentLocation");
        ref.setValue(location);
    }

    public void updateCurrentUser(User u)
    {
        if(u!=null) {
            DatabaseReference ref = mDatabase.getReference().child("users").child(mUser.getUid());
            ref.setValue(u);
        }
    }
}
