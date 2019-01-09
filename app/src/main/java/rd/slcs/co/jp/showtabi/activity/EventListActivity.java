package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

public class EventListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        PlanDisp planInfo = (PlanDisp)intent.getSerializableExtra("planDisp");
        String planName = planInfo.getPlanName();
        String startYMD = planInfo.getStartYMD();


        // 画面のタイトルを設定
        actionBar.setTitle(planName);

        // 出発日を表示
        TextView textView = findViewById(R.id.textView_startYMD);
        textView.setText(startYMD);


    }

    public void onClick(View view){
        Intent intent = new Intent(this,EventAddActivity.class);
        intent.putExtra("eventYMD","20181220");  // 日付テスト
        startActivity(intent);
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
