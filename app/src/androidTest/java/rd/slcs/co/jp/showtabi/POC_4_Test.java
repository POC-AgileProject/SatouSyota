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
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class POC_4_Test {

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
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // -------------------------------------------------------------
        // プラン登録画面が表示されていることを確認
        // -------------------------------------------------------------
        onView(withText("プラン作成")).check(matches(ViewMatchers.isDisplayed()));
    }

}
