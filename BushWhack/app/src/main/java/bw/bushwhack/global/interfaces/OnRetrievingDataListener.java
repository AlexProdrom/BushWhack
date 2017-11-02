package bw.bushwhack.global.interfaces;

import java.util.ArrayList;

import bw.bushwhack.data.models.User;
import bw.bushwhack.global.events.Error;

public interface OnRetrievingDataListener {
    void onCurrentUserRetrieved(User u);
    void onCurrentUsersRetrieved(ArrayList<User> users);
    void onErrorOccurance(Error error);
}
