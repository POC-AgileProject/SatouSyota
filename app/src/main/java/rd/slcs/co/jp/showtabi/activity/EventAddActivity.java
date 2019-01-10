package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.Plan;

/**
 * イベントの新規作成アクティビティークラスです。
 */
public class EventAddActivity extends AppCompatActivity {

    String planKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);


        // プランキーの値を取得
        Intent intentEventList = getIntent();
        planKey = intentEventList.getStringExtra(Const.DB_PLANTABLE_PLANKEY);
        Toast.makeText(this, planKey, Toast.LENGTH_LONG).show();

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventAdd);
    }

    /**
     * オプションメニューを作成する
     * @param menu  メニュー
     * @return  true（オプションメニュー表示）
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.menu_options_menu_list, menu);
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

            // 保存ボタン押下時
            case R.id.menuListOptionSave:

                // 画面の値を取得
                EditText editEventName = findViewById(R.id.editEventName);
                EditText editStartTime = findViewById(R.id.editStartTime);
                EditText editEndTime = findViewById(R.id.editEndTime);
                RadioGroup editCategory = findViewById(R.id.editCategory);
                EditText editMemo = findViewById(R.id.editMemo);
                EditText editAddress = findViewById(R.id.editAddress);

                //TODO:開始日と終了日の前後チェック
                // 入力チェック
                if ("".equals(editEventName.getText().toString())
                        || "".equals(editStartTime.getText().toString())) {

                    Toast.makeText(this, R.string.msg_error_0001, Toast.LENGTH_LONG).show();

                } else {

                    Event event = new Event();

                    // イベントの設定
                    event.setPlanKey(planKey);
                    event.setEventName(editEventName.getText().toString());
                    event.setStartTime(editStartTime.getText().toString());
                    event.setEndTime(editEndTime.getText().toString());
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
                    startActivity(intent);
                }

            // 戻るボタン押下時
            case android.R.id.home:
                finish();

            default:
        }
        return true;
    }
}
