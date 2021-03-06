package rd.slcs.co.jp.showtabi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.Util;
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
    private Date eventDate;
    private Date startTime;
    private Date endTime;
    private String eventCategory;
    private String eventMemo;
    private String eventAddress;
//    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_reference);


        // プランキーの値を取得
        Intent intentEventList = getIntent();
//        planInfo = (PlanDisp) intentEventList.getSerializableExtra("planDisp");
//        planKey = planInfo.getKey();
        eventInfo = (EventDisp) intentEventList.getSerializableExtra(Const.EVENTDISP);

        // 表示データの取得
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View v = layoutInflater.inflate(R.layout.activity_event_reference, parent, false);

//        TextView viewEventName = findViewById(R.id.viewEventName);
        TextView viewEventName = findViewById(R.id.viewEventName);
        eventName = eventInfo.getEventName();
        viewEventName.setText(eventName);

        TextView viewEventDate = findViewById(R.id.viewEventDate);
        eventDate = Util.convertToDate(eventInfo.getStartTime());
        SimpleDateFormat eventDateFmt = new SimpleDateFormat("yyyy/M/d'('E')'");
        viewEventDate.setText(eventDateFmt.format(eventDate));

        TextView viewStartTime = findViewById(R.id.viewStartTime);
        startTime = Util.convertToDate(eventInfo.getStartTime());
        SimpleDateFormat startTimeFmt = new SimpleDateFormat("H:mm");
        viewStartTime.setText(startTimeFmt.format(startTime));

        TextView viewEndTime = findViewById(R.id.viewEndTime);
        endTime = Util.convertToDate(eventInfo.getEndTime());
        SimpleDateFormat endTimeFmt = new SimpleDateFormat("H:mm");
        viewEndTime.setText(endTimeFmt.format(endTime));

        TextView viewEventCategory = findViewById(R.id.viewEventCategory);
        eventCategory = eventInfo.getCategory();
        viewEventCategory.setText(eventCategory);

        ImageView viewEventCategoryImage = findViewById(R.id.imageView_category);

        // データ定義を考慮すればこのif文で囲む必要はない。
        if(Const.categoryToIconMap.containsKey(eventCategory)) {
            viewEventCategoryImage.setImageResource(Const.categoryToIconMap.get(eventCategory));
        }

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
        actionBar.setTitle(R.string.title_eventReference);
    }

    /**
     * オプションメニューを作成する
     * @param menu  メニュー
     * @return  true（オプションメニュー表示）
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.menu_options_event_reference, menu);
        // オプションメニュー表示する場合はtrue
        return true;
    }

    /**
     * メニューのアイコン押下時
     * @param menuItem  メニューアイテム
     * @return  true
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Toast toast;

        // 押されたメニューのIDで処理を振り分ける
        switch (menuItem.getItemId()) {

            // 編集ボタン押下時
            case R.id.menuListOption_Event_Reference:

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

                    // イベント参照画面に遷移
                    Intent intent = new Intent(getApplicationContext(), EventEditActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("eventDisp",eventInfo);
                    startActivityForResult(intent, Const.SCREEN_EVENTEDIT);
                    return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK) {
            // イベント編集画面から保存ボタンを押下した際の処理
            if(Const.HANTEIKEY_SAVE == data.getIntExtra("hanteiKey", 0)) {

                setContentView(R.layout.activity_event_reference);

                // イベント情報の取得
                Intent intentEventList = getIntent();
                eventInfo = (EventDisp) data.getSerializableExtra(Const.EVENTDISP);

                TextView viewEventName = findViewById(R.id.viewEventName);
                eventName = eventInfo.getEventName();
                viewEventName.setText(eventName);

                TextView viewEventDate = findViewById(R.id.viewEventDate);
                eventDate = Util.convertToDate(eventInfo.getStartTime());
                SimpleDateFormat eventDateFmt = new SimpleDateFormat("yyyy/M/d'('E')'");
                viewEventDate.setText(eventDateFmt.format(eventDate));

                TextView viewStartTime = findViewById(R.id.viewStartTime);
                startTime = Util.convertToDate(eventInfo.getStartTime());
                SimpleDateFormat startTimeFmt = new SimpleDateFormat("H:mm");
                viewStartTime.setText(startTimeFmt.format(startTime));

                TextView viewEndTime = findViewById(R.id.viewEndTime);
                endTime = Util.convertToDate(eventInfo.getEndTime());
                SimpleDateFormat endTimeFmt = new SimpleDateFormat("H:mm");
                viewEndTime.setText(endTimeFmt.format(endTime));

                TextView viewEventCategory = findViewById(R.id.viewEventCategory);
                eventCategory = eventInfo.getCategory();
                viewEventCategory.setText(eventCategory);

                ImageView viewEventCategoryImage = findViewById(R.id.imageView_category);

                // データ定義を考慮すればこのif文で囲む必要はない。
                if(Const.categoryToIconMap.containsKey(eventCategory)) {
                    viewEventCategoryImage.setImageResource(Const.categoryToIconMap.get(eventCategory));
                }

                TextView viewEventMemo = findViewById(R.id.viewMemo);
                eventMemo = eventInfo.getMemo();
                viewEventMemo.setText(eventMemo);

                TextView viewEventAddress = findViewById(R.id.viewAddress);
                eventAddress = eventInfo.getAddress();
                viewEventAddress.setText(eventAddress);
            }
            // イベント編集画面から削除ボタンを押下した際の処理
            else if(Const.HANTEIKEY_DEL == data.getIntExtra("hanteiKey", 0)){
                finish();
            }
        }
    }

}
