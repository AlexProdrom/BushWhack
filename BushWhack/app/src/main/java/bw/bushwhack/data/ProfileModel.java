package bw.bushwhack.data;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import bw.bushwhack.data.models.User;

/**
 * Created by Dmitry on 6/15/2017.
 */

public class ProfileModel {
    private EventBus mBus = EventBus.getDefault();

    public void setCurrentUserRef() {
        DatabaseReference ref = FireBaseUtil.getInstance().getCurrentUserProfileReference();

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

    public void setCurrentTrail(String key, User user){
        if(user.getCurrentTrail() != null){
            FireBaseUtil.getInstance().getCurrentUserTrailsReference().child(user.getCurrentTrail()).child("selected").setValue(false);
        }
        FireBaseUtil.getInstance().getUserCurrentTrailReference().setValue(key);
        FireBaseUtil.getInstance().getCurrentUserTrailsReference().child(key).child("selected").setValue(true);
    }
}
