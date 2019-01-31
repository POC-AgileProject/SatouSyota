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
import org.hamcrest.Matcher;
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

public class POC_8_Test {
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

        // プラン一覧画面でプランをクリックする
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ---------------------------------------------------------------
        // イベント一覧画面に登録ボタンが表示されていることを確認する
        // ---------------------------------------------------------------
        onView(withId(R.id.menuListOption_Event_List))
                .check(matches(isDisplayed()));

        // イベント一覧画面の登録ボタンを押下する
        onView(withId(R.id.menuListOption_Event_List))
                .perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ---------------------------------------------------------------
        // イベント登録画面が表示されていることを確認する
        // ---------------------------------------------------------------
        onView(withText("イベントの新規作成")).check(matches(ViewMatchers.isDisplayed()));

        // ---------------------------------------------------------------
        // イベント登録画面に表示されている項目に過不足がないことを確認する
        // ---------------------------------------------------------------
        onView(withId(R.id.editEventName))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editEventDate))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editStartTime))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editEndTime))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editCategory))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editMemo))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editAddress))
                .check(matches(isDisplayed()));

        // ---------------------------------------------------------------
        // イベントカテゴリの表示内容を確認する
        // ---------------------------------------------------------------
        onView(withId(R.id.radio_move))
                .check(matches(isDisplayed()));
        onView(withId(R.id.radio_sightseeing))
                .check(matches(isDisplayed()));
        onView(withId(R.id.radio_eat))
                .check(matches(isDisplayed()));
        onView(withId(R.id.radio_stay))
                .check(matches(isDisplayed()));
        onView(withId(R.id.radio_other))
                .check(matches(isDisplayed()));

        // ---------------------------------------------------------------
        // 戻るボタンと保存ボタンが表示されていることを確認
        // ---------------------------------------------------------------
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .check(matches(isDisplayed()));
        onView(withId(R.id.menuListOption_Event_Add))
                .check(matches(isDisplayed()));

        // ---------------------------------------------------------------
        // 全ての項目が編集可能であることを確認する
        // ---------------------------------------------------------------
        onView(withId(R.id.editEventName))
                .perform(replaceText("テストイベント"));
        onView(withId(R.id.editEventDate))
                .perform(replaceText("20170401"));
        onView(withId(R.id.editStartTime))
                .perform(replaceText("1700"));
        onView(withId(R.id.editEndTime))
                .perform(replaceText("1900"));
        onView(withId(R.id.radio_sightseeing))
                .perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        onView(withId(R.id.editAddress))
                .perform(replaceText("東京都新宿区"));

        // ---------------------------------------------------------------
        // 保存ボタンを押下するとデータが保存され、イベント一覧画面に戻ることを確認
        // ---------------------------------------------------------------

        // 保存ボタンを押下する
        onView(withId(R.id.menuListOption_Event_Add))
                .perform(click());

        // イベント一覧画面に遷移することを確認
        onView(withText("田舎に泊まろう　第三回目")).check(matches(ViewMatchers.isDisplayed()));

        // 登録したイベントが表示されていることを確認
        RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.CardRecyclerView4Event);
        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_eventName))
                .check(matches(withText("テストイベント")));
        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_startTime))
                .check(matches(withText("17:00")));
        onView(recyclerViewMatcher
                .atPositionOnView(3, R.id.textView_endTime))
                .check(matches(withText("19:00")));
    }

    @Test
    public void case2() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // プラン一覧画面でプランをクリックする
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面の登録ボタンを押下する
        onView(withId(R.id.menuListOption_Event_List))
                .perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ---------------------------------------------------------------
        // 必須チェックが機能していることを確認する
        // ---------------------------------------------------------------

        // イベント名のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventDate))
                .perform(replaceText("20170401"));
        onView(withId(R.id.editStartTime))
                .perform(replaceText("1700"));
        onView(withId(R.id.editEndTime))
                .perform(replaceText("1900"));
        onView(withId(R.id.radio_sightseeing))
                .perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        onView(withId(R.id.editAddress))
                .perform(replaceText("東京都新宿区"));

        onView(withId(R.id.menuListOption_Event_Add))
                .perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // イベント日付のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(replaceText("テストイベント"));
        onView(withId(R.id.editEventDate))
                .perform(replaceText(""));
        onView(withId(R.id.editStartTime))
                .perform(replaceText("1700"));
        onView(withId(R.id.editEndTime))
                .perform(replaceText("1900"));
        onView(withId(R.id.radio_sightseeing))
                .perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        onView(withId(R.id.editAddress))
                .perform(replaceText("東京都新宿区"));

        onView(withId(R.id.menuListOption_Event_Add))
                .perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 開始時間のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(replaceText("テストイベント"));
        onView(withId(R.id.editEventDate))
                .perform(replaceText("20170401"));
        onView(withId(R.id.editStartTime))
                .perform(replaceText(""));
        onView(withId(R.id.editEndTime))
                .perform(replaceText("1900"));
        onView(withId(R.id.radio_sightseeing))
                .perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        onView(withId(R.id.editAddress))
                .perform(replaceText("東京都新宿区"));

        onView(withId(R.id.menuListOption_Event_Add))
                .perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));

    }

    @Test
    public void case3() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // プラン一覧画面でプランをクリックする
        onView(withId(R.id.CardRecyclerView4Plan)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面の登録ボタンを押下する
        onView(withId(R.id.menuListOption_Event_List))
                .perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベントの登録情報を入力する
        onView(withId(R.id.editEventName))
                .perform(replaceText("テストイベント"));
        onView(withId(R.id.editEventDate))
                .perform(replaceText("20170401"));
        onView(withId(R.id.editStartTime))
                .perform(replaceText("1700"));
        onView(withId(R.id.editEndTime))
                .perform(replaceText("1900"));
        onView(withId(R.id.radio_sightseeing))
                .perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        onView(withId(R.id.editAddress))
                .perform(replaceText("東京都新宿区"));

        // ---------------------------------------------------------------
        // 戻るボタンを押下してイベント情報が登録されていないことを確認する
        // ---------------------------------------------------------------
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .perform(click());

        // 登録したイベントが表示されていないことを確認
        onView(withText("テストイベント")).check(isNotDisplayed());
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
