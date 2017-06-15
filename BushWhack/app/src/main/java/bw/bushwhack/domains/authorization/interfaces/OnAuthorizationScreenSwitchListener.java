package bw.bushwhack.domains.authorization.interfaces;

import android.app.Fragment;

/**
 * Created by Dmitry on 5/11/2017.
 */

public interface OnAuthorizationScreenSwitchListener {
    // to support switching activities
    void onSwitchAuthFragment(Fragment fragment);
}

