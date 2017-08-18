package bw.bushwhack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import bw.bushwhack.data.models.Location;
import bw.bushwhack.data.models.User;
import bw.bushwhack.domains.trails.TrailPresenter;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by prodromalex on 6/18/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TrailPresenterTest {

    private TrailPresenter tp;

    @Mock
    Location l;

    @Before
    public void initialize() {
        //MockitoAnnotations.initMocks(this);
        //tp=TrailPresenter.getInstance();
    }


    @Test
    public void updateUserLocation() throws Exception {
        /*
        tp.updateUserLocation(l);
        verify(tp.getCurrentUser()).setCurrentLocation(l);
    */
    }

}
