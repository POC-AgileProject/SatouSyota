package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.os.IBinder;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rd.slcs.co.jp.showtabi.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class POC_9_Test {
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


    /**
     * イベント一覧
     * ⇒イベント編集　イベント削除
     * ⇒イベント一覧
     */
    @Test
    public void case1() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // プラン一覧画面でプランをクリックする
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面でイベントを長押しする
        onView(withId(R.id.CardRecyclerView4Event))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ---------------------------------------------------------------
        // イベント編集画面が表示されていることを確認する
        // ---------------------------------------------------------------
        onView(withText("イベントの編集画面")).check(matches(ViewMatchers.isDisplayed()));


        // ---------------------------------------------------------------
        // 削除ボタンが表示されていることを確認
        // ---------------------------------------------------------------
        onView(withId(R.id.button_del))
                .check(matches(isDisplayed()));

        // ---------------------------------------------------------------
        // 削除ボタンを押下するとデータが削除され、イベント一覧画面に戻ることを確認
        // ---------------------------------------------------------------
        // 削除ボタンを押下する
        onView(withId(R.id.button_del))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // メッセージの確認
        onView(withText(R.string.msg_warning_0002)).check(matches(ViewMatchers.isDisplayed()));

        onView(withText("はい")).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面に遷移することを確認
        onView(withText("田舎に泊まろう　第三回目")).check(matches(ViewMatchers.isDisplayed()));

        // 削除したイベントが表示されていないことを確認
        onView(withText("電車移動")).check(isNotDisplayed());

        // 削除したイベント以外が表示されていることを確認
        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.CardRecyclerView4Event);

        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_startTime))
                .check(matches(withText("12:30")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_period2))
                .check(matches(withText("～")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_endTime))
                .check(matches(withText("13:30")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_eventName))
                .check(matches(withText("お昼ご飯")));

        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_startTime))
                .check(matches(withText("14:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_period2))
                .check(matches(withText("～")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_endTime))
                .check(matches(withText("15:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_eventName))
                .check(matches(withText("機織り工場の見学")));
    }

    /**
     * イベント一覧
     * ⇒イベント参照　編集
     * ⇒イベント編集　削除
     * ⇒イベント参照？
     */
    @Test
    public void case2() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // プラン一覧画面でプランをクリックする
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面でイベントをクリックする
        onView(withId(R.id.CardRecyclerView4Event))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント参照画面で編集ボタンをクリックうする
        onView(withId(R.id.menuListOption_Event_Reference))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ---------------------------------------------------------------
        // イベント編集画面が表示されていることを確認する
        // ---------------------------------------------------------------
        onView(withText("イベントの編集画面")).check(matches(ViewMatchers.isDisplayed()));


        // ---------------------------------------------------------------
        // 削除ボタンが表示されていることを確認
        // ---------------------------------------------------------------
        onView(withId(R.id.button_del))
                .check(matches(isDisplayed()));

        // ---------------------------------------------------------------
        // 削除ボタンを押下するとデータが削除され、イベント一覧画面に戻ることを確認
        // ---------------------------------------------------------------
        // 削除ボタンを押下する
        onView(withId(R.id.button_del))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // メッセージの確認
        onView(withText(R.string.msg_warning_0002)).check(matches(ViewMatchers.isDisplayed()));

        onView(withText("はい")).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面に遷移することを確認
        onView(withText("田舎に泊まろう　第三回目")).check(matches(ViewMatchers.isDisplayed()));

        // 削除したイベントが表示されていないことを確認
        onView(withText("電車移動")).check(isNotDisplayed());

        // 削除したイベント以外が表示されていることを確認
        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.CardRecyclerView4Event);

        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_startTime))
                .check(matches(withText("12:30")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_period2))
                .check(matches(withText("～")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_endTime))
                .check(matches(withText("13:30")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_eventName))
                .check(matches(withText("お昼ご飯")));

        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_startTime))
                .check(matches(withText("14:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_period2))
                .check(matches(withText("～")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_endTime))
                .check(matches(withText("15:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_eventName))
                .check(matches(withText("機織り工場の見学")));
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
