package rd.slcs.co.jp.showtabi;


import android.app.Activity;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.activity.MainActivity;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Plan;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * POC1のテストクラス.
 */
public class POC_1_Test {

    private DatabaseReference mDatabase;
    private Activity mActivity;

    // テスト開始前に実行する処理
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class, false, false);

    @Before
    public void setUp() {
        POC_Common.setUpDB(mDatabase);
        mActivity = mActivityRule.launchActivity(null);
    }

    @After
    public void tearDown() {
        POC_Common.tearDownDB(mDatabase);
    }


    /**
     * TOP画面からプラン一覧画面に自動遷移することを確認する.<br />
     * <p>
     * TOP画面の画像表示のアサーションは@TestがonCreate後に実行されるっぽいので検証困難とした.
     */
    @Test
    public void TOP画面からプラン一覧画面に自動遷移することを確認する() {

//        ここのテスト、onCreateの処理完了後（top画面→プラン一覧画面への遷移後）に
//        評価されるっぽいので不正なテストでは？
//        一旦top画面→プラン一覧画面への遷移後に自動遷移することの検証とする。
//        // ------------------------------------------------
//        // TOP画面が表示されることを確認
//        // ------------------------------------------------
//        // TOP画面が表示されていることを確認
//        onView(withId(R.id.topLogo)).check(matches(ViewMatchers.isDisplayed()));
//        // プラン一覧画面が表示されていないことを確認
//        onView(withText("プラン一覧")).check(isNotDisplayed());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ------------------------------------------------
        // プラン一覧画面が表示されていることを確認
        // ------------------------------------------------
        // TOP画面が表示されていないことを確認
        onView(withId(R.id.topLogo)).check(isNotDisplayed());
        // プラン一覧画面が表示されることを確認
        onView(withText("プラン一覧")).check(matches(ViewMatchers.isDisplayed()));

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
