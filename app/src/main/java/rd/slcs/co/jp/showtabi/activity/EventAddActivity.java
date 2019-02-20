package rd.slcs.co.jp.showtabi.activity;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
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
import rd.slcs.co.jp.showtabi.object.PlanDisp;

/**
 * イベントの新規作成アクティビティークラスです。
 */
public class EventAddActivity extends AppCompatActivity {

    private PlanDisp planInfo;
    private String planKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);


        // プランキーの値を取得
        Intent intentEventList = getIntent();
        planInfo = (PlanDisp) intentEventList.getSerializableExtra("planDisp");
        planKey = planInfo.getKey();

        // 日付の取得
        EditText editEventDate = findViewById(R.id.editEventDate);
        String eventDate = planInfo.getStartYMD();
        editEventDate.setText(eventDate);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventAdd);
    }

    /**
     * オプションメニューを作成する
     *
     * @param menu メニュー
     * @return true（オプションメニュー表示）
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.menu_options_event_add, menu);
        // オプションメニュー表示する場合はtrue
        return true;
    }

    /**
     * メニューのアイコン押下時
     *
     * @param menuItem メニューアイテム
     * @return true
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Toast toast;

        // 押されたメニューのIDで処理を振り分ける
        switch (menuItem.getItemId()) {

            // 保存ボタン押下時
            case R.id.menuListOption_Event_Add:

                // 画面の値を取得
                EditText editEventName = findViewById(R.id.editEventName);
                EditText editEventDate = findViewById(R.id.editEventDate);
                EditText editStartTime = findViewById(R.id.editStartTime);
                EditText editEndTime = findViewById(R.id.editEndTime);
                RadioGroup editCategory = findViewById(R.id.editCategory);
                EditText editMemo = findViewById(R.id.editMemo);
                EditText editAddress = findViewById(R.id.editAddress);

                // 入力チェック
                if ("".equals(editEventName.getText().toString())
                        || "".equals(editEventDate.getText().toString())
                        || "".equals(editStartTime.getText().toString())
                        || "".equals(editEndTime.getText().toString())) {

                    Toast.makeText(this, R.string.msg_error_0001, Toast.LENGTH_LONG).show();
                    return false;

                }

                Date planStartYmd = Util.convertToDate(planInfo.getStartYMD());
                Date planEndYmd = Util.convertToDate(planInfo.getEndYMD());
                Date eventDate = Util.convertToDate(editEventDate.getText().toString());

                // 日付形式での入力チェック
                if(planStartYmd == null ||
                        planEndYmd == null ||
                        eventDate == null) {
                    Toast.makeText(this, R.string.msg_error_0002, Toast.LENGTH_LONG).show();
                    return false;
                }

                // プラン出発日・最終日とイベント日付の整合性チェック
                // イベント日付がプラン出発日より前
                if (eventDate.compareTo(planStartYmd) < 0) {
                    Toast.makeText(this, R.string.msg_error_0003, Toast.LENGTH_LONG).show();
                    return false;
                }
                // イベント日付がプラン最終日より後
                if (eventDate.compareTo(planEndYmd) > 0) {
                    Toast.makeText(this, R.string.msg_error_0004, Toast.LENGTH_LONG).show();
                    return false;
                }

                // イベント日付（文字列）
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                String sEventDate = fmt.format(eventDate);

                // 日付と時間の連結
                String startTime = sEventDate + editStartTime.getText().toString();
                String endTime ="";
                // 終了時間が入力されている場合
                if(!"".equals(editEndTime.getText().toString())) {
                    endTime = sEventDate + editEndTime.getText().toString();
                }

                Event event = new Event();

                // イベントの設定
                event.setPlanKey(planKey);
                event.setEventName(editEventName.getText().toString());
                event.setStartTime(startTime);
                event.setEndTime(endTime);
                event.setMemo(editMemo.getText().toString());
                event.setAddress(editAddress.getText().toString());

                int checkedId = editCategory.getCheckedRadioButtonId();
                if(checkedId != -1) {
                    RadioButton radioButton = (RadioButton)findViewById(checkedId);
                    event.setCategory(radioButton.getText().toString());
                }

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE);

                //push()でキーの自動生成
                mDatabase.push().setValue(event);

                // イベントリスト画面に遷移
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Const.PLANDISP,planInfo);
                startActivity(intent);

                break;

            // 戻るボタン押下時
            case android.R.id.home:
                finish();
                break;

            default:
        }
        return true;
    }

    /**
     * 時計アイコン押下時
     *
     * @param view
     */
    public void showTimePickDialog(View view) {

        final int textView_id = view.getId();

        //TimePicker選択時の処理
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {

                TextView textView = null;

                if (textView_id == R.id.TimePicker_startTime) {
                    textView = findViewById(R.id.editStartTime);
                } else if (textView_id == R.id.TimePicker_endTime) {
                    textView = findViewById(R.id.editEndTime);
                }

                String strHour = String.format("%02d", hour);
                String strMinute = String.format("%02d", minute);

                textView.setText(strHour + strMinute);
            }
        };

        TimePickerDialog dialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                listener, Const.DEFAULT_HOUR, Const.DEFAULT_MINUTE, true);
        dialog.show();

    }

}
