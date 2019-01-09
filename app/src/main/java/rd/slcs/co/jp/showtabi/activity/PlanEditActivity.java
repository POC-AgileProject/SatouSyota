package rd.slcs.co.jp.showtabi.activity;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Plan;

public class PlanEditActivity extends AppCompatActivity {

    private String planKey;

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

                // プラン画像が設定されている場合
                if(plan.getIcon() != null){
                    // DBから取得した64bitエンコードされている画像ファイルをBitmapにエンコード
                    decodedString = Base64.decode(plan.getIcon(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ImageView imageView_icon = findViewById(R.id.imageView_icon);
                    imageView_icon.setImageBitmap(decodedByte);
                }



                EditText editPlanName = findViewById(R.id.editPlanName);
                editPlanName.setText(plan.getPlanName());

                EditText editStartDay = findViewById(R.id.editStartDay);
                editStartDay.setText(plan.getStartYMD());

                EditText editEndDay = findViewById(R.id.editEndDay);
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

    public void onClickDelButton(View v) {

        new AlertDialog.Builder(PlanEditActivity.this)
                .setTitle(R.string.alertDialog_title)
                .setMessage(R.string.msg_warning_0001)
                .setPositiveButton(
                        R.string.alertDialog_positiveButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE + "/" + planKey);
                                mDatabase.removeValue();

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


    }

    public void onClickSaveButton(View v) {

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


        //TODO:開始日と終了日の前後チェック
        // 入力チェック
        if ("".equals(plan.getPlanName()) || "".equals(plan.getStartYMD())) {

            Toast.makeText(this, R.string.msg_error_0001, Toast.LENGTH_LONG).show();

        } else {

            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE + "/" + planKey);

            mDatabase.setValue(plan);

            Intent intent = new Intent(getApplicationContext(), PlanListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
