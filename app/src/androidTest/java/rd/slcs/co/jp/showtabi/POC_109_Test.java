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

public class POC_109_Test {
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
        onView(withId(R.id.CardRecyclerView4Plan)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面で１番目のイベントを長押しする
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
        // 登録済みのイベントの内容が表示されることを確認する
        // ---------------------------------------------------------------
        onView(withId(R.id.editEventName))
                .check(matches(withText("電車移動")));
        onView(withId(R.id.editEventDate))
                .check(matches(withText("20170401")));
        onView(withId(R.id.editStartTime))
                .check(matches(withText("1000")));
        onView(withId(R.id.editEndTime))
                .check(matches(withText("1230")));
        onView(withId(R.id.editCategory))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editMemo))
                .check(matches(withText("特急あずさ７号（９番ホーム発）")));
        onView(withId(R.id.editAddress))
                .check(matches(withText("")));

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
        // 戻るボタンと保存ボタンと削除ボタンが表示されていることを確認
        // ---------------------------------------------------------------
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button_save))
                .check(matches(isDisplayed()));
        onView(withId(R.id.button_del))
                .check(matches(isDisplayed()));

        // ---------------------------------------------------------------
        // 全ての項目が編集可能であることを確認する
        // ---------------------------------------------------------------
        onView(withId(R.id.editEventName))
                .perform(replaceText("テストイベント"));
        onView(withId(R.id.editEventDate))
                .perform(replaceText("20170403"));
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

        // 正しく編集されているか確認する
        // カテゴリについては目視確認
        onView(withId(R.id.editEventName))
                .check(matches(withText("テストイベント")));
        onView(withId(R.id.editEventDate))
                .check(matches(withText("20170403")));
        onView(withId(R.id.editStartTime))
                .check(matches(withText("1700")));
        onView(withId(R.id.editEndTime))
                .check(matches(withText("1900")));
        onView(withId(R.id.editMemo))
                .check(matches(withText("テストメモ")));
        onView(withId(R.id.editAddress))
                .check(matches(withText("東京都新宿区")));
    }
}
