package bw.bushwhack.domains.trails.activeview.interfaces;

import java.util.List;

import bw.bushwhack.data.models.Marker;

/**
 * Created by Dmitry on 6/17/2017.
 */

public interface CurrentTrailCallback {

    void onRetrievedMarkers(List<Marker> markerList);
}
