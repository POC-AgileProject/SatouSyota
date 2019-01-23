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
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class POC_11_Test {
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

        // プラン一覧画面でプランをクリックする
        onView(withId(R.id.CardRecyclerView4Plan)) .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面でイベントをクリックする
        onView(withId(R.id.CardRecyclerView4Event))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ---------------------------------------------------------------
        // イベント参照画面が表示されていることを確認する
        // ---------------------------------------------------------------
        onView(withText("イベントの参照画面")).check(matches(ViewMatchers.isDisplayed()));

        // ---------------------------------------------------------------
        // イベント参照画面に項目が正しく表示されることを確認する
        // ---------------------------------------------------------------
        onView(withId(R.id.viewEventName))
                .check(matches(withText("電車移動")));
        onView(withId(R.id.viewEventDate))
                .check(matches(withText("20170401")));
        onView(withId(R.id.viewStartTime))
                .check(matches(withText("1000")));
        onView(withId(R.id.viewEndTime))
                .check(matches(withText("1230")));
        onView(withId(R.id.viewEventCategory))
                .check(matches(withText("移動")));
        onView(withId(R.id.viewMemo))
                .check(matches(withText("特急あずさ７号（９番ホーム発）")));
        onView(withId(R.id.viewAddress))
                .check(matches(withText("")));

        // ---------------------------------------------------------------
        // 戻るボタンと編集ボタンが表示されていることを確認
        // ---------------------------------------------------------------
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .check(matches(isDisplayed()));
        onView(withId(R.id.menuListOption_Event_Reference))
                .check(matches(isDisplayed()));

        // ---------------------------------------------------------------
        // イベント参照画面から編集ボタンを押下したらイベント編集画面に遷移することを確認
        // ---------------------------------------------------------------

        // 編集ボタンを押下する
        onView(withId(R.id.menuListOption_Event_Reference))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント編集画面に遷移することを確認
        onView(withText("イベントの編集画面")).check(matches(ViewMatchers.isDisplayed()));

        // イベント参照画面が表示されていないことを確認する
        onView(withText("イベントの参照画面")).check(isNotDisplayed());

        // イベント内容が正しく表示されていることを確認
        onView(withId(R.id.editEventName))
                .check(matches(withText("電車移動")));
        onView(withId(R.id.editEventDate))
                .check(matches(withText("20170401")));
        onView(withId(R.id.editStartTime))
                .check(matches(withText("1000")));
        onView(withId(R.id.editEndTime))
                .check(matches(withText("1230")));
        onView(withId(R.id.editCategory))
                .check(matches(withText("移動")));
        onView(withId(R.id.editMemo))
                .check(matches(withText("特急あずさ７号（９番ホーム発）")));
        onView(withId(R.id.editAddress))
                .check(matches(withText("")));

        // ---------------------------------------------------------------
        // イベント編集画面から戻るボタンを押下したらイベント参照画面に遷移することを確認
        // ---------------------------------------------------------------
        // 編集画面の内容編集
        onView(withId(R.id.editEventName))
                .perform(replaceText("電車移動２"));
        onView(withId(R.id.editEventDate))
                .perform(replaceText("20180402"));
        onView(withId(R.id.editStartTime))
                .perform(replaceText("1730"));
        onView(withId(R.id.editEndTime))
                .perform(replaceText("1900"));
        onView(withId(R.id.radio_sightseeing))
                .perform(click());
        onView(withId(R.id.editMemo))
                .perform(replaceText("急行あずさ８号（１０番ホーム発）"));
        onView(withId(R.id.editAddress))
                .perform(replaceText("東京都新宿区"));

        // 戻るボタンを押下する
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント参照画面が表示されていることを確認する
        onView(withText("イベントの参照画面")).check(matches(ViewMatchers.isDisplayed()));

        // イベント編集画面が表示されていないことを確認する
        onView(withText("イベントの編集画面")).check(isNotDisplayed());

        // イベント内容が正しく表示されていることを確認
        onView(withId(R.id.editEventName))
                .check(matches(withText("電車移動")));
        onView(withId(R.id.editEventDate))
                .check(matches(withText("20170401")));
        onView(withId(R.id.editStartTime))
                .check(matches(withText("1000")));
        onView(withId(R.id.editEndTime))
                .check(matches(withText("1230")));
        onView(withId(R.id.editCategory))
                .check(matches(withText("移動")));
        onView(withId(R.id.editMemo))
                .check(matches(withText("特急あずさ７号（９番ホーム発）")));
        onView(withId(R.id.editAddress))
                .check(matches(withText("")));

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
