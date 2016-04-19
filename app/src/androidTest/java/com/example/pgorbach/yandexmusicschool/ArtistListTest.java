package com.example.pgorbach.yandexmusicschool;

import org.hamcrest.Matchers;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.AllOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.KeyEvent;
import android.widget.EditText;

import com.example.pgorbach.yandexmusicschool.api.content.Artist;

import java.util.Map;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class ArtistListTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
    }

    @Test
    public void changeText_sameActivity() {

        Espresso.onView(ViewMatchers.withId(R.id.artist_list))
                .check(ViewAssertions.matches((ViewMatchers.hasDescendant(ViewMatchers.withText("Usher")))))
                .perform(ViewActions.click())
        ;
        //        // Check that the text was changed.
//        Espresso.onView(ViewMatchers.withText("Usher"))
//                .check(ViewAssertions.matches(ViewMatchers.withText("Usher")));

//        Espresso.onView(ViewMatchers.withContentDescription("Navigate up")).perform(ViewActions.click());
        Espresso.pressBack();
//        Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.action_search)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(EditText.class)).perform(ViewActions.typeText("nirvana"), ViewActions.pressKey(KeyEvent.KEYCODE_ENTER));

        Espresso.onView(ViewMatchers.withId(R.id.artist_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
//        // Type text and then press the button.
//        Espresso.onView(ViewMatchers.withId(R.id.editTextUserInput))
//                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
//        Espresso.onView(withId(R.id.changeTextBt)).perform(click());
//
//        // Check that the text was changed.
//        onView(withId(R.id.textToBeChanged))
//                .check(matches(withText(mStringToBetyped)));
    }
}

