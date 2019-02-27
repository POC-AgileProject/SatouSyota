package rd.slcs.co.jp.showtabi;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rd.slcs.co.jp.showtabi.activity.MainActivity;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.EventDisp;
import rd.slcs.co.jp.showtabi.object.Photo;
import rd.slcs.co.jp.showtabi.object.Plan;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * プラン削除時にひも付くイベント情報を削除されていることを確認するテストクラス.
 */
public class POC_141_Test {
    private DatabaseReference mDatabase;
    private Activity mActivity;

    // テスト開始前に実行する処理
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class, false, false);

    @Before
    public void setUp() {
        POC_Common.setUpDB(mDatabase);
        mActivity = mActivityRule.launchActivity(null);
    }

    @After
    public void tearDown() {
        POC_Common.tearDownDB(mDatabase);
    }

    // TODO 不要なので実装完了したら消します
    @Test
    public void DETETE_THIS_METHOD() {
        assertEquals(1,1);
    }

    // TODO テストコード一旦捨てる
//    @Test
    public void testプラン削除時に紐づくイベントと写真データがDBから削除されること() {
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

        // イベント一覧画面でイベント長押ししたらイベント編集画面が開くことを確認
        onView(withId(R.id.CardRecyclerView4Event)).perform(RecyclerViewActions.actionOnItemAtPosition(1, longClick()));

        // TODO 削除実施


        // 写真データの削除確認
//        assertDeletePhotos();
    }

    // TODO テストコード一旦捨てる
//    @Test
    public void testイベント削除時に紐づく写真データがDBから削除されること() {
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

        // イベント一覧画面でイベント長押ししたらイベント編集画面が開くことを確認
        onView(withId(R.id.CardRecyclerView4Event)).perform(RecyclerViewActions.actionOnItemAtPosition(1, longClick()));

        // TODO 削除実施


        // 写真データの削除確認
        assertDeletePhotos();
    }

    /**
     * イベント削除時に紐づく写真データのみが削除されていることを確認します.
     */
    private void assertDeletePhotos() {
        // Firebaseからインスタンスを取得
        DatabaseReference mDatabase = getFirebasePhotoTable(Const.DB_PHOTOSTABLE);

        //  Eventsテーブルから選択されたプランに該当するイベントを抽出
        Query query = mDatabase.orderByChild(Const.DB_PHOTOSTABLE_PHOTOKEY);  // 昇順降順のやり方が不明

        // クエリを使用してデータベースの内容を一度だけ取得する
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Photoテーブルの写真枚数が1枚であることを確認
                assertEquals(1, (int) snapshot.getChildrenCount());

                // 写真のソートキーで別イベントの写真が残っていることを確認
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Photo photo = dataSnapshot.getValue(Photo.class);
                    assertEquals("20190112", photo.getSortKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // nop
            }
        });
    }

    // TODO 未実装
    /**
     * プラン削除時に紐づくEvent,Photoデータが削除されていること.
     */
    private void assertDeletePlan() {
        // Firebaseからインスタンスを取得
        DatabaseReference mDatabase = getFirebasePhotoTable(Const.DB_PHOTOSTABLE);

        //  Eventsテーブルから選択されたプランに該当するイベントを抽出
        Query query = mDatabase.orderByChild(Const.DB_PHOTOSTABLE_PHOTOKEY);  // 昇順降順のやり方が不明

        // クエリを使用してデータベースの内容を一度だけ取得する
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Photoテーブルの写真枚数が1枚であることを確認
                assertEquals(1, (int) snapshot.getChildrenCount());

                // 写真のソートキーで別イベントの写真が残っていることを確認
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Photo photo = dataSnapshot.getValue(Photo.class);
                    assertEquals("20190112", photo.getSortKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // nop
            }
        });
    }

    // TODO WIP
    private void countChildPhotoNumber(String parentEventKey) {
        // Firebaseからインスタンスを取得
        DatabaseReference mDatabase = getFirebasePhotoTable(Const.DB_PHOTOSTABLE);

        //  Eventsテーブルから選択されたプランに該当するイベントを抽出
        Query query = mDatabase.orderByChild(Const.DB_PHOTOSTABLE_PHOTOKEY);  // 昇順降順のやり方が不明

        // クエリを使用してデータベースの内容を一度だけ取得する
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Photoテーブルの写真枚数が1枚であることを確認
                assertEquals(0, (int) snapshot.getChildrenCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // nop
            }
        });
    }

    // TODO
    // 数をゲットする処理は難しそう
    // 引数で与えた数と合致するかどうか、という方針で実装してみる　→　無理
    // もう個別assertionするしかないのか、、、、、
//    private int countChildEventNumber(final String parentPlanKey, int num) {
//        int childEventNumber = 0;
//
//        // Firebaseからインスタンスを取得
//        DatabaseReference mDatabase = getFirebasePhotoTable(Const.DB_EVENTTABLE);
//
//        //  Eventsテーブルから選択されたプランに該当するイベントを抽出
//        Query query = mDatabase.orderByChild(Const.DB_EVENTTABLE_EVENTKEY);
//
//        // クエリを使用してデータベースの内容を一度だけ取得する
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                // Photoテーブルの写真枚数が1枚であることを確認
//                assertEquals(0, (int) snapshot.getChildrenCount());
//
////                return (int) snapshot.getChildrenCount();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Event event = dataSnapshot.getValue(Event.class);
//                    if (parentPlanKey.equals(event.getPlanKey()) {
////                        childEventNumber++;
//                    }
////                    if (num == 1){
////
////                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // nop
//            }
//        });
//
//        return childEventNumber;
//    }

    /**
     * テーブルを操作するFIrebaseオブジェクトを取得します.
     *
     * @param targetTable   捜査対象テーブル文字列
     * @return  FireBaseオブジェクト
     */
    private DatabaseReference getFirebasePhotoTable(String targetTable) {
        return FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + targetTable);
    }
}
