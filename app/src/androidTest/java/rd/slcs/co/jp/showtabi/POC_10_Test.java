package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.os.IBinder;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.Root;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
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
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
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
                .perform(replaceText("20500101"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20510101"));
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.menuListOption_Plan_Add))
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
        String eventDate = "20500102"; // 日付引継ぎ確認用

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
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // イベント編集画面への遷移を確認
        onView(withText("イベントの編集画面")).check(matches(ViewMatchers.isDisplayed()));

        //        ※イベント一覧画面の日付を初期設定を確認
        onView(withId(R.id.editEventDate)).check(matches(isEditTextValueEqualTo(eventDate)));

        // ---------------------------------------------------------------
        // ボタン表示の確認
        // ---------------------------------------------------------------
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .check(matches(isDisplayed()));
        onView(withId(R.id.menuListOption_Event_Edit_Save))
                .check(matches(isDisplayed()));
        onView(withId(R.id.menuListOption_Event_Edit_Del))
                .check(matches(isDisplayed()));


        // -----------------------------------------------------------------------------------
        // バリデーションチェック
        // -----------------------------------------------------------------------------------

        // イベント名のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(nestedScrollTo(), replaceText(""));
        onView(withId(R.id.radio_stay))
                .perform(nestedScrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(nestedScrollTo(), replaceText("20500103"));

        onView(withId(R.id.editStartTime))
                .perform(nestedScrollTo(), replaceText("1900"));
        onView(withId(R.id.editEndTime))
                .perform(nestedScrollTo(), replaceText("2100"));
        onView(withId(R.id.editMemo))
                .perform(nestedScrollTo(), replaceText("メモ１"));
        onView(withId(R.id.editAddress))
                .perform(replaceText("東京都新宿区１"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 日付のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(nestedScrollTo(), replaceText("テストイベント名２"));
        onView(withId(R.id.radio_move))
                .perform(nestedScrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(nestedScrollTo(), replaceText(""));
        onView(withId(R.id.editStartTime))
                .perform(nestedScrollTo(), replaceText("1900"));
        onView(withId(R.id.editEndTime))
                .perform(nestedScrollTo(), replaceText("2100"));
        onView(withId(R.id.editMemo))
                .perform(nestedScrollTo(), replaceText("メモ２"));
        onView(withId(R.id.editAddress))
                .perform(nestedScrollTo(), replaceText("東京都新宿区２"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 開始時間のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(nestedScrollTo(), replaceText("テストイベント名３"));
        onView(withId(R.id.radio_sightseeing))
                .perform(nestedScrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(nestedScrollTo(), replaceText("20500103"));
        onView(withId(R.id.editStartTime))
                .perform(nestedScrollTo(), replaceText(""));
        onView(withId(R.id.editEndTime))
                .perform(nestedScrollTo(), replaceText("2100"));
        onView(withId(R.id.editMemo))
                .perform(nestedScrollTo(), replaceText("メモ３"));
        onView(withId(R.id.editAddress))
                .perform(nestedScrollTo(), replaceText("東京都新宿区３"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());

        onView(withText(R.string.msg_error_0001)).inRoot(new ToastMatcher())
                .check(matches(withText("必須項目を入力してください")));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 終了時間のみを未入力で登録ボタンを押下する
        onView(withId(R.id.editEventName))
                .perform(nestedScrollTo(), replaceText("テストイベント名4"));
        onView(withId(R.id.radio_sightseeing))
                .perform(nestedScrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(nestedScrollTo(), replaceText("20990331"));
        onView(withId(R.id.editStartTime))
                .perform(nestedScrollTo(), replaceText("1700"));
        onView(withId(R.id.editEndTime))
                .perform(nestedScrollTo(), replaceText(""));
        onView(withId(R.id.editMemo))
                .perform(nestedScrollTo(), replaceText("メモ３"));
        onView(withId(R.id.editAddress))
                .perform(nestedScrollTo(), replaceText("東京都新宿区３"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());

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
        String testEventDate = "20500103";
        String testEventStartTime = "0400";
        String testEventEndTime = "2300";
        String testEventMemo = "メモ４";
        String testEventAddress = "東京都新宿区４";
        onView(withId(R.id.editEventName))
                .perform(nestedScrollTo(), replaceText(testEventName));
        onView(withId(R.id.radio_move))
                .perform(nestedScrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(nestedScrollTo(), replaceText(testEventDate));
        onView(withId(R.id.editStartTime))
                .perform(nestedScrollTo(), replaceText(testEventStartTime));
        onView(withId(R.id.editEndTime))
                .perform(nestedScrollTo(), replaceText(testEventEndTime));
        onView(withId(R.id.editMemo))
                .perform(nestedScrollTo(), replaceText(testEventMemo));
        onView(withId(R.id.editAddress))
                .perform(nestedScrollTo(), replaceText(testEventAddress));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());


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
                .perform(replaceText("20500101"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20510101"));
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.menuListOption_Plan_Add))
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
        String eventDate = "20500102"; // 日付引継ぎ確認用

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


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String testEventName = "テスト編集イベント名４";
        String testEventDate = "20500103";
        String testEventStartTime = "0400";
        String testEventEndTime = "2300";
        String testEventMemo = "メモ４";
        String testEventAddress = "東京都新宿区４";
        onView(withId(R.id.editEventName))
                .perform(nestedScrollTo(), replaceText(testEventName));
        onView(withId(R.id.radio_move))
                .perform(nestedScrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(nestedScrollTo(), replaceText(testEventDate));
        onView(withId(R.id.editStartTime))
                .perform(nestedScrollTo(), replaceText(testEventStartTime));
        onView(withId(R.id.editEndTime))
                .perform(nestedScrollTo(), replaceText(testEventEndTime));
        onView(withId(R.id.editMemo))
                .perform(nestedScrollTo(), replaceText(testEventMemo));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editAddress))
                .perform(nestedScrollTo(), replaceText(testEventAddress));

        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 登録したイベントが変更されていないことを確認
        onView(withText("テストイベント")).check(matches(ViewMatchers.isDisplayed()));
    }

    // TODO 参照画面→編集画面→変更を保存→参照画面で内容がリロードされないバグがあり、対応完了すれば@Testアノテーション外してください
    @Test
    public void case3() {
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
                .perform(replaceText("20500101"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20510101"));
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));
        // 保存ボタンを押下
        onView(withId(R.id.menuListOption_Plan_Add))
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
        String eventDate = "20500102"; // 日付引継ぎ確認用

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


        // イベント参照画面を開く
        onView(withId(R.id.CardRecyclerView4Event)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 編集画面に遷移
        onView(withId(R.id.menuListOption_Event_Reference)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // -----------------------------------------------------------------------------------
        // 表示されている項目は全て登録した内容が表示されていること
        // -----------------------------------------------------------------------------------

        String testEventName = "テスト編集イベント名４";
        String testEventDate = "20500103";
        String testEventStartTime = "0400";
        String testEventEndTime = "2300";
        String testEventMemo = "メモ４";
        String testEventAddress = "東京都新宿区４";
        onView(withId(R.id.editEventName))
                .perform(nestedScrollTo(), replaceText(testEventName));
        onView(withId(R.id.radio_move))
                .perform(nestedScrollTo(), click());
        onView(withId(R.id.editEventDate))
                .perform(nestedScrollTo(), replaceText(testEventDate));
        onView(withId(R.id.editStartTime))
                .perform(nestedScrollTo(), replaceText(testEventStartTime));
        onView(withId(R.id.editEndTime))
                .perform(nestedScrollTo(), replaceText(testEventEndTime));
        onView(withId(R.id.editMemo))
                .perform(nestedScrollTo(), replaceText(testEventMemo));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editAddress))
                .perform(nestedScrollTo(), replaceText(testEventAddress));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());


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
    public void testイベント日付の範囲チェックの確認() {

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
                .perform(replaceText("20500102"));
        onView(withId(R.id.editEndDay))
                .perform(replaceText("20500105"));
        onView(withId(R.id.editPerson))
                .perform(replaceText("100"));
        onView(withId(R.id.editMemo))
                .perform(replaceText("テストメモ"));

        // 保存ボタンを押下
        onView(withId(R.id.menuListOption_Plan_Add))
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
        String eventDate = "20500103"; // 日付引継ぎ確認用

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

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String wrongEventDate = "2050010"; // 桁数不足（不正な入力値）
        String beforeEventDate = "20500101"; // プラン出発前
        String afterEventDate = "20500106"; // プラン最終日後

        String message_wrong = "日付形式で入力してください";    // 日付形式以外の入力値のエラーメッセージ
        String message_before = "出発日以降の日付を入力してください";    // プラン出発前のエラーメッセージ
        String message_after = "最終日以前の日付を入力してください";    // プラン最終日後のエラーメッセージ

        // ---------------------------------------------------------------
        // イベント日付の数値チェック（日付形式で入力されているかのチェック）
        // ---------------------------------------------------------------
        onView(withId(R.id.editEventDate))
                .perform(replaceText(wrongEventDate));

        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());

        onView(withText(R.string.msg_error_0002)).inRoot(new ToastMatcher())
                .check(matches(withText(message_wrong)));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ---------------------------------------------------------------
        // プラン出発前のチェック
        // ---------------------------------------------------------------

        onView(withId(R.id.editEventDate))
                .perform(replaceText(beforeEventDate));

        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());

        onView(withText(R.string.msg_error_0003)).inRoot(new ToastMatcher())
                .check(matches(withText(message_before)));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ---------------------------------------------------------------
        // プラン最終日後ののチェック
        // ---------------------------------------------------------------
        onView(withId(R.id.editEventDate))
                .perform(replaceText(afterEventDate));

        onView(withId(R.id.menuListOption_Event_Edit_Save)).perform(click());

        onView(withText(R.string.msg_error_0004)).inRoot(new ToastMatcher())
                .check(matches(withText(message_after)));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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

    public static Matcher<View> isEditTextValueEqualTo(final String content) {

        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("Match Edit Text Value with View ID Value : :  " + content);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView) && !(view instanceof EditText)) {
                    return false;
                }
                if (view != null) {
                    String text;
                    if (view instanceof TextView) {
                        text = ((TextView) view).getText().toString();
                    } else {
                        text = ((EditText) view).getText().toString();
                    }

                    return (text.equalsIgnoreCase(content));
                }
                return false;
            }
        };
    }

    public static ViewAction nestedScrollTo() {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(
                        isDescendantOfA(isAssignableFrom(NestedScrollView.class)),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
            }

            @Override
            public String getDescription() {
                return "View is not NestedScrollView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                try {
                    NestedScrollView nestedScrollView = (NestedScrollView)
                            findFirstParentLayoutOfClass(view, NestedScrollView.class);
                    if (nestedScrollView != null) {
                        nestedScrollView.scrollTo(0, view.getTop());
                    } else {
                        throw new Exception("Unable to find NestedScrollView parent.");
                    }
                } catch (Exception e) {
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(e)
                            .build();
                }
                uiController.loopMainThreadUntilIdle();
            }

        };
    }

    private static View findFirstParentLayoutOfClass(View view, Class<? extends View> parentClass) {
        ViewParent parent = new FrameLayout(view.getContext());
        ViewParent incrementView = null;
        int i = 0;
        while (parent != null && !(parent.getClass() == parentClass)) {
            if (i == 0) {
                parent = findParent(view);
            } else {
                parent = findParent(incrementView);
            }
            incrementView = parent;
            i++;
        }
        return (View) parent;
    }

    private static ViewParent findParent(View view) {
        return view.getParent();
    }

    private static ViewParent findParent(ViewParent view) {
        return view.getParent();
    }

}
