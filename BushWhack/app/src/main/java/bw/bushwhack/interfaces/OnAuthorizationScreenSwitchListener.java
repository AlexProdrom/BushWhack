package bw.bushwhack.interfaces;

import android.support.v4.app.Fragment;

/**
 * Created by Dmitry on 5/11/2017.
 */

public interface OnAuthorizationScreenSwitchListener {
    // to support switching activities
    void onSwitchAuthFragment(Fragment fragment);
}

