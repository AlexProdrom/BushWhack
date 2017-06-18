package bw.bushwhack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import bw.bushwhack.data.models.Location;
import bw.bushwhack.data.models.Trail;
import bw.bushwhack.data.models.User;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
    private User u;

    @Mock
    Trail t;

    @Before
    public void initialize()
    {
        u=new User();
    }

    @Test
    public void addTrail() throws Exception {
        u.addTrail(t);
    }
}
