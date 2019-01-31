package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class POC_38_Test {
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
        // ＋ボタンを押下
        onView(withId(R.id.menuListOption_Plan_List))
                .perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 登録するプラン情報を入力
        onView(withId(R.id.editPlanName))
                .perform(replaceText("テストプラン"));
        onView(withId(R.id.editStartDay))
                .perform(replaceText("20500101"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20510101"));
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.button_save))
                .perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ------------------------------------------------
        // プラン一覧画面に登録したプランが表示されていることを確認
        // ------------------------------------------------
        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.CardRecyclerView4Plan);

        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_planName))
                .check(matches(withText("田舎に泊まろう　第一回目")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_startYMD))
                .check(matches(withText("20170401")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_endYMD))
                .check(matches(withText("20170403")));

        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_planName))
                .check(matches(withText("田舎に泊まろう　第二回目")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_startYMD))
                .check(matches(withText("20181010")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_endYMD))
                .check(matches(withText("20181012")));

        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_planName))
                .check(matches(withText("田舎に泊まろう　第三回目")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_startYMD))
                .check(matches(withText("20191115")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_endYMD))
                .check(matches(withText("")));

        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_planName))
                .check(matches(withText("テストプラン")));
        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_startYMD))
                .check(matches(withText("20500101")));
        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_endYMD))
                .check(matches(withText("20510101")));
    }
}
