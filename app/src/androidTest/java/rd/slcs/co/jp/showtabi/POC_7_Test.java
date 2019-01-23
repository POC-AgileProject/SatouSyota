package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class POC_7_Test {

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
        // １番目のプランをクリック
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ------------------------------------------------
        // イベント一覧画面が表示されていることを確認
        // ------------------------------------------------
        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.CardRecyclerView4Event);

        // プラン名が表示されていることを確認
        onView(withText("田舎に泊まろう　第一回目")).check(matches(ViewMatchers.isDisplayed()));
        // プラン開始日が初期表示されていることを確認
        onView(withText("2017/04/01")).check(matches(ViewMatchers.isDisplayed()));

        // イベント一覧画面の表示内容を確認
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_startTime))
                .check(matches(withText("10:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_period2))
                .check(matches(withText("～")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_endTime))
                .check(matches(withText("12:30")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_eventName))
                .check(matches(withText("電車移動")));

        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_startTime))
                .check(matches(withText("12:30")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_period2))
                .check(matches(withText("～")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_endTime))
                .check(matches(withText("13:30")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_eventName))
                .check(matches(withText("お昼ご飯")));

        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_startTime))
                .check(matches(withText("14:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_period2))
                .check(matches(withText("～")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_endTime))
                .check(matches(withText("15:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_eventName))
                .check(matches(withText("機織り工場の見学")));

        // １番目のイベントを長押し
        onView(withId(R.id.CardRecyclerView4Event)) .perform(RecyclerViewActions.actionOnItemAtPosition(0,longClick()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント情報の編集
        onView(withId(R.id.editStartTime))
                .perform(replaceText("1500"));
        onView(withId(R.id.editEndTime))
                .perform(replaceText("1600"));

        // 保存ボタンを押下
        onView(withId(R.id.button_save))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ------------------------------------------------
        // イベント一覧画面のソートを確認
        // ------------------------------------------------
        recyclerViewMatcher = new RecyclerViewMatcher(R.id.CardRecyclerView4Event);

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

        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_startTime))
                .check(matches(withText("15:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_period2))
                .check(matches(withText("～")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_endTime))
                .check(matches(withText("16:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_eventName))
                .check(matches(withText("電車移動")));

    }
}
