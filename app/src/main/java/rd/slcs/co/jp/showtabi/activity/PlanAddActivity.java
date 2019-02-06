package rd.slcs.co.jp.showtabi.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.DatePickerDialogFragment;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Plan;

public class PlanAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView editStartDay;
    private TextView editEndDay;

    private int id_clickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);

        editStartDay = findViewById(R.id.viewStartDay);
        editEndDay = findViewById(R.id.viewEndDay);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_planAdd);

    }


    /**
     * 保存ボタン押下時処理
     * @param v    View
     */
    public void onClickSaveButton(View v) {

        Plan plan = new Plan();

        EditText editPlanName = findViewById(R.id.editPlanName);
        plan.setPlanName(editPlanName.getText().toString());

        editStartDay = findViewById(R.id.viewStartDay);
        plan.setStartYMD(editStartDay.getText().toString());

        editEndDay = findViewById(R.id.viewEndDay);
        plan.setEndYMD(editEndDay.getText().toString());

        EditText editPerson = findViewById(R.id.editPerson);
        plan.setPerson(editPerson.getText().toString());

        EditText editMemo = findViewById(R.id.editMemo);
        plan.setMemo(editMemo.getText().toString());


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

    }

    /**
     * 日付入力項目押下時
     * @param v
     */
    public void showDatePickerDialog(View v) {

        // 出発日、最終日の識別するIDを取得
        id_clickDate = v.getId();

        DialogFragment newFragment = new DatePickerDialogFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
        if(R.id.viewStartDay == id_clickDate) {
            editStartDay.setText( strYear + strMonth +  strDate);
        }
        else if (R.id.viewEndDay == id_clickDate) {
            editEndDay.setText( strYear + strMonth +  strDate);
        }
    }

    /*
        メニューのアイコンが押下された場合の処理を行います。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int itemID = item.getItemId();

        if(itemID == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
