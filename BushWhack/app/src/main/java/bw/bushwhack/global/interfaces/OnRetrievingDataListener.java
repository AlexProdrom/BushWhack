package bw.bushwhack.global.interfaces;

import java.util.ArrayList;

import bw.bushwhack.data.models.User;

/**
 * Created by prodromalex on 6/6/2017.
 */

public interface OnRetrievingDataListener {
    void onCurrentUserRetrieved(User u);
    void onCurrentUsersRetrieved(ArrayList<User> users);
    void onErrorOccurance(Error error);
}
