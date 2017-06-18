package bw.bushwhack.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Dmitry on 6/15/2017.
 */

public class FireBaseUtil {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private FireBaseUtil() {
        try {

            this.mFirebaseAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        } catch (Exception e) {
            Log.e("Reference setup", e.getMessage());
        }
    }

    private static class SingletonHolder {
        private static final FireBaseUtil INSTANCE = new FireBaseUtil();
    }


    public static FireBaseUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // is used to get the instances
    public FirebaseAuth getFirebaseAuth() {
        return this.mFirebaseAuth;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return this.mDatabase;
    }

    public DatabaseReference getDatabaseReference() {
        return this.mDatabaseReference;
    }

    public DatabaseReference getCurrentUserProfileReference(){
        FirebaseUser currentUser = this.getFirebaseAuth().getCurrentUser();
        DatabaseReference ref = this.getDatabaseReference().child("users").child(currentUser.getUid());
        return ref;
    }

    public DatabaseReference getCurrentUserTrailsReference(){
        DatabaseReference ref = this.getCurrentUserProfileReference().child("trails");
        return ref;
    }

    public DatabaseReference getCurrentUserSingleTrailReference(String trailKey){
        DatabaseReference ref = this.getCurrentUserTrailsReference().child(trailKey);
        return ref;
    }

    public DatabaseReference getAllUsersReference(){
        return this.getDatabaseReference().child("users");
    }

    public DatabaseReference getUserCurrentTrailKeyReference(){
        return this.getCurrentUserProfileReference().child("currentTrail");
    }

    public DatabaseReference getUserTrailMarkersReference(String trailKey){
        return this.getCurrentUserTrailsReference().child(trailKey).child("markers");
    }
}
