package bw.bushwhack.data;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    /**
     * Deletes the trail if it is not the current trail of the user
     * @param key
     * @param user
     * @return
     */
    public boolean deleteTrail(String key, User user){
        if(!key.equals(user.getCurrentTrail())){

            // just remove it
            FireBaseUtil.getInstance().getCurrentUserTrailsReference().child(key).removeValue();
            return true;
        }else{
            return false;
        }
    }

    public void setCurrentTrail(String key, User user){
        if(user.getCurrentTrail() != null){
            if(user.getCurrentTrail().length()>0 && !user.getCurrentTrail().equals("")){

                FireBaseUtil.getInstance().getCurrentUserTrailsReference().child(user.getCurrentTrail()).child("selected").setValue(false);
            }
        }
        FireBaseUtil.getInstance().getUserCurrentTrailKeyReference().setValue(key);
        FireBaseUtil.getInstance().getCurrentUserTrailsReference().child(key).child("selected").setValue(true);
    }

    //Image upload tryout
    public void uploadImage()
    {
        StorageReference strg= FirebaseStorage.getInstance().getReference();
    }
}
