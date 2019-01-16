package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.EventDisp;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

/**
 * イベントの参照アクティビティークラスです。
 */
public class EventReferenceActivity extends AppCompatActivity {

    private PlanDisp planInfo;
    private String planKey;
    private EventDisp eventInfo;
    private String eventName;
    private String eventDate;
    private String startTime;
    private String endTime;
    private String eventCategory;
    private String eventMemo;
    private String eventAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);


        // プランキーの値を取得
        Intent intentEventList = getIntent();
//        planInfo = (PlanDisp) intentEventList.getSerializableExtra("planDisp");
//        planKey = planInfo.getKey();
        eventInfo = (EventDisp) intentEventList.getSerializableExtra(Const.DB_EVENTTABLE_EVENTKEY);

        // 表示データの取得
        TextView viewEventName = findViewById(R.id.viewEventName);
        eventName = eventInfo.getEventName();
        viewEventName.setText(eventName);

        TextView viewEventDate = findViewById(R.id.viewEventDate);
        eventDate = eventInfo.getStartTime();
        viewEventDate.setText(eventDate);

        TextView viewStartTime = findViewById(R.id.viewStartTime);
        startTime = eventInfo.getStartTime();
        viewStartTime.setText(startTime);

        TextView viewEndTime = findViewById(R.id.viewEndTime);
        endTime = eventInfo.getEndTime();
        viewEndTime.setText(endTime);

        TextView viewEventCategory = findViewById(R.id.viewEventCategory);
        eventCategory = eventInfo.getCategory();
        viewEventCategory.setText(eventCategory);

        TextView viewEventMemo = findViewById(R.id.viewMemo);
        eventMemo = eventInfo.getMemo();
        viewEventMemo.setText(eventMemo);

        TextView viewEventAddress = findViewById(R.id.viewAddress);
        eventAddress = eventInfo.getAddress();
        viewEventAddress.setText(eventAddress);



        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventAdd);
    }

//    /**
//     * オプションメニューを作成する
//     * @param menu  メニュー
//     * @return  true（オプションメニュー表示）
//     */
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // menuにcustom_menuレイアウトを適用
//        getMenuInflater().inflate(R.menu.menu_options_event_add, menu);
//        // オプションメニュー表示する場合はtrue
//        return true;
//    }

    /**
     * メニューのアイコン押下時
     * @param menuItem  メニューアイテム
     * @return  true
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Toast toast;

        // 押されたメニューのIDで処理を振り分ける
        switch (menuItem.getItemId()) {

//            // 保存ボタン押下時
//            case R.id.menuListOption_Event_Add:
//
//                // 画面の値を取得
//                EditText editEventName = findViewById(R.id.editEventName);
//                EditText editEventDate = findViewById(R.id.editEventDate);
//                EditText editStartTime = findViewById(R.id.editStartTime);
//                EditText editEndTime = findViewById(R.id.editEndTime);
//                RadioGroup editCategory = findViewById(R.id.editCategory);
//                EditText editMemo = findViewById(R.id.editMemo);
//                EditText editAddress = findViewById(R.id.editAddress);
//
//                //TODO:開始日と終了日の前後チェック
//                // 入力チェック
//                if ("".equals(editEventName.getText().toString())
//                        || "".equals(editEventDate.getText().toString())
//                        || "".equals(editStartTime.getText().toString())) {
//
//                    Toast.makeText(this, R.string.msg_error_0001, Toast.LENGTH_LONG).show();
//
//                } else {
//
//                    // 日付と時間の連結
//                    String startTime = eventDate + editStartTime.getText().toString();
//                    String endTime ="";
//                    // 終了時間が入力されている場合
//                    if(!"".equals(editEndTime.getText().toString())) {
//                        endTime = eventDate + editEndTime.getText().toString();
//                    }
//
//                    Event event = new Event();
//
//                    // イベントの設定
//                    event.setPlanKey(planKey);
//                    event.setEventName(editEventName.getText().toString());
//                    event.setStartTime(startTime);
//                    event.setEndTime(endTime);
//                    event.setMemo(editMemo.getText().toString());
//                    event.setAddress(editAddress.getText().toString());
//
//                    int checkedId = editCategory.getCheckedRadioButtonId();
//                    if(checkedId != -1) {
//                        RadioButton radioButton = (RadioButton)findViewById(checkedId);
//                        event.setCategory(radioButton.getText().toString());
//                    }
//
//                    DatabaseReference mDatabase;
//                    mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE);
//
//                    //push()でキーの自動生成
//                    mDatabase.push().setValue(event);
//
//                    // イベントリスト画面に遷移
//                    Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("planDisp",planInfo);
//                    startActivity(intent);
//                }
//                break;

            // 戻るボタン押下時
            case android.R.id.home:
                finish();
                break;

            default:
        }
        return true;
    }
}
