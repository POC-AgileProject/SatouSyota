package rd.slcs.co.jp.showtabi.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.DatePickerDialogFragment;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.Util;
import rd.slcs.co.jp.showtabi.common.firebase.PlanRemover;
import rd.slcs.co.jp.showtabi.object.Plan;

public class PlanEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private String planKey;

    /** 出発日 */
    private EditText editStartDay;
    /** 最終日 */
    private EditText editEndDay;
    /** プランアイコン */
    private String planIcon;
    /** 押下ボタン判別キー */
    private int id_clickDate;
    /** 日付初期設定フラグ */
    private boolean dateSetFlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_edit);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_planEdit);


        Intent intent = getIntent();
        planKey = intent.getStringExtra(Const.DB_PLANTABLE_PLANKEY);


        // DBから値を取得し表示
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE + "/" + planKey);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Plan plan = snapshot.getValue(Plan.class);


                // 画面にプランの情報を表示
                byte[] decodedString = {};

                planIcon = plan.getIcon();

                // プラン画像が設定されている場合
                if(planIcon != null && !"".equals(planIcon)){
                    // DBから取得した64bitエンコードされている画像ファイルをBitmapにエンコード
                    decodedString = Base64.decode(plan.getIcon(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ImageView imageView_icon = findViewById(R.id.imageView_icon);
                    imageView_icon.setImageBitmap(decodedByte);
                }

                EditText editPlanName = findViewById(R.id.editPlanName);
                editPlanName.setText(plan.getPlanName());

                editStartDay = findViewById(R.id.editStartDay);
                editStartDay.setText(plan.getStartYMD());

                editEndDay = findViewById(R.id.editEndDay);
                editEndDay.setText(plan.getEndYMD());

                EditText editPerson = findViewById(R.id.editPerson);
                editPerson.setText(plan.getPerson());

                EditText editMemo = findViewById(R.id.editMemo);
                editMemo.setText(plan.getMemo());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * オプションメニューを作成する
     * @param menu  メニュー
     * @return  true（オプションメニュー表示）
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.menu_options_plan_edit, menu);
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

    /*
        メニューのアイコンが押下された場合の処理を行います。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int itemID = item.getItemId();

        // 押されたメニューのIDで処理を振り分ける
        switch (itemID) {

            // 保存ボタン押下時
            case R.id.menuListOption_Plan_Edit_Save:

                Plan plan = new Plan();

                EditText editPlanName = findViewById(R.id.editPlanName);
                plan.setPlanName(editPlanName.getText().toString());

                EditText editStartDay = findViewById(R.id.editStartDay);
                plan.setStartYMD(editStartDay.getText().toString());

                EditText editEndDay = findViewById(R.id.editEndDay);
                plan.setEndYMD(editEndDay.getText().toString());

                EditText editPerson = findViewById(R.id.editPerson);
                plan.setPerson(editPerson.getText().toString());

                EditText editMemo = findViewById(R.id.editMemo);
                plan.setMemo(editMemo.getText().toString());

                plan.setIcon(planIcon);

                //TODO:開始日と終了日の前後チェック
                // 入力チェック
                if ("".equals(plan.getPlanName())
                        || "".equals(plan.getStartYMD())
                        || "".equals(plan.getEndYMD())) {

                    Toast.makeText(this, R.string.msg_error_0001, Toast.LENGTH_LONG).show();

                } else {

                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE + "/" + planKey);

                    mDatabase.setValue(plan);

                    Intent intent = new Intent(getApplicationContext(), PlanListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;

            // 削除ボタン押下時
            case R.id.menuListOption_Plan_Edit_Del:

                new AlertDialog.Builder(PlanEditActivity.this)
                        .setTitle(R.string.alertDialog_title)
                        .setMessage(R.string.msg_warning_0001)
                        .setPositiveButton(
                                R.string.alertDialog_positiveButton,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removePlan(planKey);

                                        Intent intent = new Intent(PlanEditActivity.this, PlanListActivity.class);
                                        startActivity(intent);

                                    }
                                })
                        .setNegativeButton(
                                R.string.alertDialog_negativeButton,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .show();

                break;

            // 戻るボタン押下時
            case android.R.id.home:
                finish();
                break;

            default:
        }

        return super.onOptionsItemSelected(item);
    }


    private void removePlan(String planKey) {
        PlanRemover planRemover = new PlanRemover(planKey);
        planRemover.removePlan();
    }

}
