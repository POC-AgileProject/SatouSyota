package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.os.IBinder;
import android.support.test.espresso.Espresso;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class POC_10_Test {
    private DatabaseReference mDatabase;
    private Activity mActivity;

    // テスト開始前に実行する処理
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class, false, false);

    @Before
    public void setUp() {
        POC_Common.tearDownDB(mDatabase);
        mActivity = mActivityRule.launchActivity(null);
    }

    ;

    @After
    public void tearDown() {
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
        onView(withId(R.id.tourokuButton))
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


        // ---------------------------------------------------------------
        // 適当にイベント登録
        // ---------------------------------------------------------------
        String eventDate = "20170401"; // 日付引継ぎ確認用

        onView(withId(R.id.editEventName))
                .perform(replaceText("テストイベント"));
        onView(withId(R.id.editEventDate))
                .perform(replaceText(eventDate));
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

        // 保存ボタンを押下する
        onView(withId(R.id.menuListOption_Event_Add))
                .perform(click());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // イベント一覧画面でイベント長押ししたらイベント編集画面が開くことを確認
        onView(withId(R.id.CardRecyclerView4Event)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント編集画面への遷移を確認
        onView(withText("イベントの編集画面")).check(matches(ViewMatchers.isDisplayed()));

        //        ※イベント一覧画面の日付を初期設定を確認
        onView(withText(R.id.editEventDate)).check(matches(withText("20500101")));


        // ---------------------------------------------------------------
        // ボタン表示の確認
        // ---------------------------------------------------------------
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .check(matches(isDisplayed()));


        Espresso.closeSoftKeyboard();
        onView(withText("保存"))
                .check(matches(isDisplayed()));
        onView(withText("削除"))
                .check(matches(isDisplayed()));


        // -----------------------------------------------------------------------------------
        // バリデーションチェック
        // -----------------------------------------------------------------------------------

        // イベント名のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(scrollTo(), replaceText(""));
        onView(withId(R.id.radio_stay))
                .perform(scrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(scrollTo(), replaceText("20990331"));
        onView(withId(R.id.editStartTime))
                .perform(scrollTo(), replaceText("1900"));
        onView(withId(R.id.editEndTime))
                .perform(scrollTo(), replaceText("2100"));
        onView(withId(R.id.editMemo))
                .perform(scrollTo(), replaceText("メモ１"));
        onView(withId(R.id.editAddress))
                .perform(scrollTo(), replaceText("東京都新宿区１"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.button_save)).perform(scrollTo(), click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 日付のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(scrollTo(), replaceText("テストイベント名２"));
        onView(withId(R.id.radio_move))
                .perform(scrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(scrollTo(), replaceText(""));
        onView(withId(R.id.editStartTime))
                .perform(scrollTo(), replaceText("1900"));
        onView(withId(R.id.editEndTime))
                .perform(scrollTo(), replaceText("2100"));
        onView(withId(R.id.editMemo))
                .perform(scrollTo(), replaceText("メモ２"));
        onView(withId(R.id.editAddress))
                .perform(scrollTo(), replaceText("東京都新宿区２"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button_save)).perform(scrollTo(), click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 開始時間のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(scrollTo(), replaceText("テストイベント名３"));
        onView(withId(R.id.radio_sightseeing))
                .perform(scrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(scrollTo(), replaceText("20990331"));
        onView(withId(R.id.editStartTime))
                .perform(scrollTo(), replaceText(""));
        onView(withId(R.id.editEndTime))
                .perform(scrollTo(), replaceText("2100"));
        onView(withId(R.id.editMemo))
                .perform(scrollTo(), replaceText("メモ３"));
        onView(withId(R.id.editAddress))
                .perform(scrollTo(), replaceText("東京都新宿区３"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button_save)).perform(scrollTo(), click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // -----------------------------------------------------------------------------------
        // 表示されている項目は全て登録した内容が表示されていること
        // -----------------------------------------------------------------------------------

        String testEventName = "テスト編集イベント名４";
        String testEventDate = "29990331";
        String testEventStartTime = "0400";
        String testEventEndTime = "2300";
        String testEventMemo = "メモ４";
        String testEventAddress = "東京都新宿区４";
        onView(withId(R.id.editEventName))
                .perform(scrollTo(), replaceText(testEventName));
        onView(withId(R.id.radio_move))
                .perform(scrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(scrollTo(), replaceText(testEventDate));
        onView(withId(R.id.editStartTime))
                .perform(scrollTo(), replaceText(testEventStartTime));
        onView(withId(R.id.editEndTime))
                .perform(scrollTo(), replaceText(testEventEndTime));
        onView(withId(R.id.editMemo))
                .perform(scrollTo(), replaceText(testEventMemo));
        onView(withId(R.id.editAddress))
                .perform(scrollTo(), replaceText(testEventAddress));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button_save)).perform(scrollTo(), click());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント一覧画面に遷移することを確認
        onView(withText("テストプラン")).check(matches(ViewMatchers.isDisplayed()));

        // イベント一覧画面でイベントをクリックする
        onView(withId(R.id.CardRecyclerView4Event))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(3000);
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
                .check(matches(withText(testEventName)));
        onView(withId(R.id.viewEventDate))
                .check(matches(withText(testEventDate)));
        onView(withId(R.id.viewStartTime))
                .check(matches(withText(testEventStartTime)));
        onView(withId(R.id.viewEndTime))
                .check(matches(withText(testEventEndTime)));
        onView(withId(R.id.viewEventCategory))
                .check(matches(withText("移動")));
        onView(withId(R.id.viewMemo))
                .check(matches(withText(testEventMemo)));
        onView(withId(R.id.viewAddress))
                .check(matches(withText(testEventAddress)));


    }

    @Test
    public void case2() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ＋ボタンを押下
        onView(withId(R.id.tourokuButton))
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


        // ---------------------------------------------------------------
        // 適当にイベント登録
        // ---------------------------------------------------------------
        String eventDate = "20170401"; // 日付引継ぎ確認用

        onView(withId(R.id.editEventName))
                .perform(replaceText("テストイベント"));
        onView(withId(R.id.editEventDate))
                .perform(replaceText(eventDate));
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

        // 保存ボタンを押下する
        onView(withId(R.id.menuListOption_Event_Add))
                .perform(click());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // イベント一覧画面でイベント長押ししたらイベント編集画面が開く
        onView(withId(R.id.CardRecyclerView4Event)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));


        String testEventName = "テスト編集イベント名４";
        String testEventDate = "20210331";
        String testEventStartTime = "0400";
        String testEventEndTime = "2300";
        String testEventMemo = "メモ４";
        String testEventAddress = "東京都新宿区４";
        onView(withId(R.id.editEventName))
                .perform(scrollTo(), replaceText(testEventName));
        onView(withId(R.id.radio_move))
                .perform(scrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(scrollTo(), replaceText(testEventDate));
        onView(withId(R.id.editStartTime))
                .perform(scrollTo(), replaceText(testEventStartTime));
        onView(withId(R.id.editEndTime))
                .perform(scrollTo(), replaceText(testEventEndTime));
        onView(withId(R.id.editMemo))
                .perform(scrollTo(), replaceText(testEventMemo));
        onView(withId(R.id.editAddress))
                .perform(scrollTo(), replaceText(testEventAddress));

        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 登録したイベントが変更されていないことを確認
        onView(withText("テストイベント")).check(matches(ViewMatchers.isDisplayed()));
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
