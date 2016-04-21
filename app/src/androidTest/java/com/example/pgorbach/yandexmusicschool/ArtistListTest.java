package com.example.pgorbach.yandexmusicschool;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.AllOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;


import android.support.test.espresso.matcher.BoundedMatcher;
import static org.hamcrest.core.Is.is;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;
import com.orhanobut.logger.Logger;

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


        String artistName = "Usher";
        String searchString = "Nirvana";
        Matcher<View> mRecyclerViewMatcher = ViewMatchers.withId(R.id.artist_list);

        Espresso.onView(mRecyclerViewMatcher).perform(
                RecyclerViewActions.scrollToPosition(10));
        Espresso.onView(ViewMatchers.withId(R.id.fab_up)).check(
                ViewAssertions.matches(withVisible(is(View.GONE))));

        Espresso.onView(mRecyclerViewMatcher).perform(
                RecyclerViewActions.scrollToPosition(0));
        Espresso.onView(ViewMatchers.withId(R.id.fab_up)).check(
                ViewAssertions.matches(withVisible(is(View.GONE))));


        Espresso.onView(mRecyclerViewMatcher)
                .perform(RecyclerViewActions.actionOnItem(
                        ViewMatchers.hasDescendant(ViewMatchers.withText(artistName)), ViewActions.click()));

        Espresso.onView(ViewMatchers.isAssignableFrom(CollapsingToolbarLayout.class))
                .check(ViewAssertions.matches(withCollapsingToolbarLayoutTitle(is((CharSequence) artistName))));

        //        // Check that the text was changed.
//        Espresso.onView(ViewMatchers.withText("Usher"))
//                .check(ViewAssertions.matches(ViewMatchers.withText("Usher")));

//        Espresso.onView(ViewMatchers.withContentDescription("Navigate up")).perform(ViewActions.click());
        Espresso.pressBack();
//        Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.action_search)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(EditText.class)).perform(ViewActions.typeText(searchString), ViewActions.pressKey(KeyEvent.KEYCODE_ENTER));

        Espresso.onView(mRecyclerViewMatcher).check(hasItemsCount(1));

        Espresso.onView(mRecyclerViewMatcher).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        Espresso.onView(ViewMatchers.isAssignableFrom(CollapsingToolbarLayout.class))
                .check(ViewAssertions.matches(withCollapsingToolbarLayoutTitle(is((CharSequence) searchString))));

//        // Type text and then press the button.
//        Espresso.onView(ViewMatchers.withId(R.id.editTextUserInput))
//                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
//        Espresso.onView(withId(R.id.changeTextBt)).perform(click());
//
//        // Check that the text was changed.
//        onView(withId(R.id.textToBeChanged))
//                .check(matches(withText(mStringToBetyped)));
    }




    public static ViewAssertion hasItemsCount(final int count) {
        return new ViewAssertion() {
            @Override public void check(View view, NoMatchingViewException e) {
                if (!(view instanceof RecyclerView)) {
                    throw e;
                }
                RecyclerView rv = (RecyclerView) view;
                Assert.assertThat(rv.getAdapter().getItemCount(), is(count));
            }
        };
    }

 

    private static Matcher<Object> withVisible(final Matcher<Integer> visibilityMatcher) {
        return new BoundedMatcher<Object, View>(View.class) {
            @Override public boolean matchesSafely(View view) {
                return visibilityMatcher.matches(view.getVisibility());
            }
            @Override public void describeTo(Description description) {
                description.appendText("does not equals correct visibility: ");
                visibilityMatcher.describeTo(description);
            }
        };
    }

    private static Matcher<Object> withCollapsingToolbarLayoutTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {
            @Override public boolean matchesSafely(CollapsingToolbarLayout toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }
}

