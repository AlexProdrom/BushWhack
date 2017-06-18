package bw.bushwhack.domains.authorization;


import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bw.bushwhack.R;
import bw.bushwhack.data.FireBaseUtil;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@LargeTest

@RunWith(AndroidJUnit4.class)
public class LoginActivityToQuitTest {

    @Before
    public void logout(){
        if(FireBaseUtil.getInstance().getFirebaseAuth().getCurrentUser()!=null){
            FireBaseUtil.getInstance().getFirebaseAuth().signOut();
        }
    }

    @Before
    public void grantPhonePermission(){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant "+getTargetContext().getPackageName()
                            +" android.permission.ACCESS_COARSE_LOCATION");
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant "+getTargetContext().getPackageName()
                            +" android.permission.ACCESS_FINE_LOCATION");
        }
    }


    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityToQuitTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.input_email), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.input_email), isDisplayed()));
        appCompatEditText2.perform(replaceText("abc@abc.abc"), closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.input_password), isDisplayed()));
        appCompatEditText3.perform(replaceText("abcabc"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_login), withText("Login"), isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.text_name), withText("Abc"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Abc")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.text_nickname), withText("abc@abc.abc"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("abc@abc.abc")));

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.action_settings), withContentDescription("Quit"), isDisplayed()));
        bottomNavigationItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction editText = onView(
                allOf(withId(R.id.input_email),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(TextInputLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
