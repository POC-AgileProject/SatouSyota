package rd.slcs.co.jp.showtabi.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.DatePickerDialogFragment;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.Util;
import rd.slcs.co.jp.showtabi.object.Plan;

public class PlanAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /** 出発日 */
    private EditText editStartDay;
    /** 最終日 */
    private EditText editEndDay;
    /** 押下ボタン判別キー */
    private int id_clickDate;
    /** 日付初期設定フラグ */
    private boolean dateSetFlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);

        editStartDay = findViewById(R.id.editStartDay);
        editEndDay = findViewById(R.id.editEndDay);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_planAdd);

        // 人数を選択するSpinnerを初期化
        Spinner spinner = (Spinner) findViewById(R.id.PersonNumber);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.person_number_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    /**
     * オプションメニューを作成する
     * @param menu  メニュー
     * @return  true（オプションメニュー表示）
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.menu_options_plan_add, menu);
        // オプションメニュー表示する場合はtrue
        return true;
    }

    /**
     * カレンダーアイコン押下時
     * @param v
     */
    public void showDatePickerDialog(View v) {

        // 初期化
        dateSetFlg = false;

        // 出発日、最終日の識別するIDを取得
        id_clickDate = v.getId();

        // 出発日の場合
        if(R.id.bottom_DatePicker_startDay == id_clickDate) {
            Date planStartDay = Util.convertToDate(editStartDay.getText().toString());
            // 日付形式で入力されていない場合
            if (planStartDay == null) {
                DialogFragment newFragment = new DatePickerDialogFragment((Activity)this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
            // 日付形式で入力されている場合
            else {
                DialogFragment newFragment = new DatePickerDialogFragment((Activity)this,planStartDay);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }

            // 日付初期設定フラグの更新
            Date planEndDay = Util.convertToDate(editEndDay.getText().toString());
            // 日付形式で入力されていない場合
            if (planEndDay == null) {
                dateSetFlg = true;
            }
        }
        // 最終日の場合
        else if (R.id.bottom_DatePicker_endDay == id_clickDate) {
            Date planEndDay = Util.convertToDate(editEndDay.getText().toString());
            // 日付形式で入力されていない場合
            if (planEndDay == null) {
                DialogFragment newFragment = new DatePickerDialogFragment((Activity)this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
            // 日付形式で入力されている場合
            else {
                DialogFragment newFragment = new DatePickerDialogFragment((Activity)this,planEndDay);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }

            // 日付初期設定フラグの更新
            Date planStartDay = Util.convertToDate(editStartDay.getText().toString());
            // 日付形式で入力されていない場合
            if (planStartDay == null) {
                dateSetFlg = true;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String strYear = String.valueOf(year);
        String strMonth = "00";
        String strDate = "00";
        if(monthOfYear + 1 < 10) {
            strMonth = "0" + String.valueOf(monthOfYear + 1);
        }
        else {
            strMonth = String.valueOf(monthOfYear + 1);
        }
        if (dayOfMonth < 10) {
            strDate = "0" + String.valueOf(dayOfMonth);
        }else {
            strDate = String.valueOf(dayOfMonth);
        }

        // 出発日の場合
        if(R.id.bottom_DatePicker_startDay == id_clickDate) {
            editStartDay.setText( strYear + strMonth +  strDate);
            if(dateSetFlg){
                editEndDay.setText( strYear + strMonth +  strDate);
            }
        }
        // 最終日の場合
        else if (R.id.bottom_DatePicker_endDay == id_clickDate) {
            editEndDay.setText( strYear + strMonth +  strDate);
            if(dateSetFlg){
                editStartDay.setText( strYear + strMonth +  strDate);
            }
        }
    }

    /**
     *メニューのアイコンが押下された場合の処理を行います。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int itemID = item.getItemId();

        // 押されたメニューのIDで処理を振り分ける
        switch (itemID) {

            // 保存ボタン押下時
            case R.id.menuListOption_Plan_Add:

                Plan plan = new Plan();

                EditText editPlanName = findViewById(R.id.editPlanName);
                plan.setPlanName(editPlanName.getText().toString());

                editStartDay = findViewById(R.id.editStartDay);
                plan.setStartYMD(editStartDay.getText().toString());

                editEndDay = findViewById(R.id.editEndDay);
                plan.setEndYMD(editEndDay.getText().toString());

                Spinner spinner = (Spinner) findViewById(R.id.PersonNumber);
                String selectedNumber = spinner.getSelectedItem().toString();
                plan.setPerson(selectedNumber);

                EditText editMemo = findViewById(R.id.editMemo);
                plan.setMemo(editMemo.getText().toString());

                // 作成時点では空
                plan.setIcon("");

                //TODO:開始日と終了日の前後チェック
                // 入力チェック
                if ("".equals(plan.getPlanName())
                        || "".equals(plan.getStartYMD())
                        || "".equals(plan.getEndYMD())) {

                    Toast.makeText(this, R.string.msg_error_0001, Toast.LENGTH_LONG).show();

                } else {

                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE );

                    //push()でキーの自動生成
                    mDatabase.push().setValue(plan);

                    Intent intent = new Intent(getApplicationContext(), PlanListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;

            // 戻るボタン押下時
            case android.R.id.home:
                finish();
                break;

            default:
        }

        return super.onOptionsItemSelected(item);
    }

}
