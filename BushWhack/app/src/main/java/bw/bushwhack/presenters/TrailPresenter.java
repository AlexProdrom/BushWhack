package bw.bushwhack.presenters;

import org.greenrobot.eventbus.EventBus;

import bw.bushwhack.models.DataModel;
import bw.bushwhack.views.CurrentTrailView;

/**
 * Created by prodromalex on 6/3/2017.
 */

public class TrailPresenter {
    private volatile static TrailPresenter uniqueInstance;

    public static TrailPresenter getInstance() {
        if (uniqueInstance == null) {
            synchronized (TrailPresenter.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TrailPresenter(new DataModel(), new CurrentTrailView());
                }
            }
        }
        return uniqueInstance;
    }

    private DataModel mModel;
    private CurrentTrailView mView;
    private EventBus mBus = EventBus.getDefault();

    private TrailPresenter(DataModel model, CurrentTrailView view) {
        this.mModel = model;
        this.mView = view;
        mBus.register(this);
    }
}
