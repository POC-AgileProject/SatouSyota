package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rd.slcs.co.jp.showtabi.activity.MainActivity;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Plan;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class POC_47_Test {
    private DatabaseReference mDatabase;
    private Activity mActivity;

    // テスト開始前に実行する処理
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class,false,false);

    @Before
    public void setUp(){
        POC_Common.setUpDB(mDatabase);
        mActivity = mActivityRule.launchActivity(null);
    };

    @After
    public void tearDown(){
        POC_Common.tearDownDB(mDatabase);
    }

    @Test
    public void case1() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ２番目のプランを長押し
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(1,longClick()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ------------------------------------------------
        // プラン編集画面から←ボタンで戻れることを確認
        // ------------------------------------------------
        // プラン編集画面が表示されることを確認
        onView(withText("プランの編集画面")).check(matches(ViewMatchers.isDisplayed()));
        // プラン一覧画面が表示されていないことを確認
        onView(withText("プラン一覧")).check(isNotDisplayed());

        //←ボタンを押下
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ------------------------------------------------
        // プラン一覧画面が表示されていることを確認
        // ------------------------------------------------
        // プラン一覧画面が表示されることを確認
        onView(withText("プラン一覧")).check(matches(ViewMatchers.isDisplayed()));
        // プラン編集画面が表示されていないことを確認
        onView(withText("プランの編集画面")).check(isNotDisplayed());
    }

    public static ViewAssertion isNotDisplayed() {
        return new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noView) {
                if (view != null && isDisplayed().matches(view)) {
                    throw new AssertionError("View is present in the hierarchy and Displayed: "
                            + HumanReadables.describe(view));
                }
            }
        };
    }
}
