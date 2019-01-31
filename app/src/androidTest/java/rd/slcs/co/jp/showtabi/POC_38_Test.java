package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
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
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ＋ボタンを押下
        onView(withId(R.id.menuListOption_Plan_List))
                .perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 登録するプラン情報を入力
        onView(withId(R.id.editPlanName))
                .perform(replaceText("テストプラン"));
        onView(withId(R.id.editStartDay))
                .perform(replaceText("20170501"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20170505"));
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.button_save))
                .perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ------------------------------------------------
        // プラン一覧画面に登録したプランが表示されていることを確認
        // ------------------------------------------------
        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.CardRecyclerView4Plan);

        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_planName))
                .check(matches(withText("田舎に泊まろう　第三回目")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_startYMD))
                .check(matches(withText("20191224")));
        onView(recyclerViewMatcher
                .atPositionOnView(0, R.id.textView_endYMD))
                .check(matches(withText("20191231")));

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
                .check(matches(withText("田舎に泊まろう　第一回目")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_startYMD))
                .check(matches(withText("20180410")));
        onView(recyclerViewMatcher
                .atPositionOnView(2, R.id.textView_endYMD))
                .check(matches(withText("20180420")));

        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_planName))
                .check(matches(withText("テストプラン")));
        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_startYMD))
                .check(matches(withText("20170501")));
        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_endYMD))
                .check(matches(withText("20170505")));
    }

    /**
     * プラン登録処理　必須チェック
     */
    @Test
    public void case2() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // -------------------------------------------------------------
        // プラン一覧画面が表示されていることを確認
        // -------------------------------------------------------------
        onView(withText("プラン一覧")).check(matches(ViewMatchers.isDisplayed()));

        // -------------------------------------------------------------
        // ＋ボタンを押下してプラン登録画面に遷移することを確認
        // -------------------------------------------------------------
        // ＋ボタンを押下
        onView(withId(R.id.menuListOption_Plan_List))
                .perform(click());
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
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.button_save))
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
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.button_save))
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
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.button_save))
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
