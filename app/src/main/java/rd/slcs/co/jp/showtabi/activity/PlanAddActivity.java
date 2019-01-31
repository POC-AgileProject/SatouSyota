package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Plan;

public class PlanAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_planAdd);

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
