package bw.bushwhack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import bw.bushwhack.data.models.Location;
import bw.bushwhack.data.models.User;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationTest {

    private User u;
    @Mock
    Location l;

    @Before
    public void initialize()
    {
        u=new User();
    }

    @Test
    public void androidLocation() throws Exception {
        when(l.getAndroidLocation())
                .thenReturn(new android.location.Location(""));

        u=new User();
        u.setCurrentLocation(l);

        assertThat(u.getCurrentLocation().getAndroidLocation(),instanceOf(android.location.Location.class));
    }
}