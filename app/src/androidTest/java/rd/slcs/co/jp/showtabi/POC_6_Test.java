package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rd.slcs.co.jp.showtabi.activity.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

public class POC_6_Test {
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
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ２番目のプランを長押し
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(1,longClick()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // プラン情報を編集
        onView(withId(R.id.editPlanName))
                .perform(replaceText("テストプラン"));
        onView(withId(R.id.editStartDay))
                .perform(replaceText("20170501"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20170505"));
        onView(withId(R.id.PersonNumber)).perform(click());
        onData(anything()).atPosition(6).perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.menuListOption_Plan_Edit_Save))
                .perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ------------------------------------------------
        // プラン一覧画面に編集後のプラン情報が表示されていることを確認
        // ------------------------------------------------
        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.CardRecyclerView4Plan);

        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_planName))
                .check(matches(withText("田舎に泊まろう　第三回目")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_startYMD))
                .check(matches(withText("2019/12/24(火)")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_endYMD))
                .check(matches(withText("2019/12/31(火)")));

        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_planName))
                .check(matches(withText("田舎に泊まろう　第一回目")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_startYMD))
                .check(matches(withText("2018/4/10(火)")));
        onView(recyclerViewMatcher
                .atPositionOnView(1, R.id.textView_endYMD))
                .check(matches(withText("2018/4/20(金)")));

        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_planName))
                .check(matches(withText("テストプラン")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_startYMD))
                .check(matches(withText("2017/5/1(月)")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_endYMD))
                .check(matches(withText("2017/5/5(金)")));
    }

    /**
     * プラン編集処理　必須チェック
     */
    @Test
    public void case2() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ２番目のプランを長押し
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(1,longClick()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // プラン名の必須チェック
        onView(withId(R.id.editPlanName))
                .perform(replaceText(""));
        onView(withId(R.id.editStartDay))
                .perform(replaceText("20170501"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20170505"));
        onView(withId(R.id.PersonNumber)).perform(click());
        onData(anything()).atPosition(6).perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.menuListOption_Plan_Edit_Save))
                .perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 出発日の必須チェック
        onView(withId(R.id.editPlanName))
                .perform(replaceText("テストプラン"));
        onView(withId(R.id.editStartDay))
                .perform(replaceText(""));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20170505"));
        onView(withId(R.id.PersonNumber)).perform(click());
        onData(anything()).atPosition(6).perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.menuListOption_Plan_Edit_Save))
                .perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 最終日の必須チェック
        onView(withId(R.id.editPlanName))
                .perform(replaceText("テストプラン"));
        onView(withId(R.id.editStartDay))
                .perform(replaceText("20170501"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText(""));
        onView(withId(R.id.PersonNumber)).perform(click());
        onData(anything()).atPosition(6).perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.menuListOption_Plan_Edit_Save))
                .perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));

    }


    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    return true;
                    //means this window isn't contained by any other windows.
                }
            }
            return false;
        }
    }
}
