package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

public class EventListActivity extends AppCompatActivity {


    private PlanDisp planInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        planInfo = (PlanDisp)intent.getSerializableExtra("planDisp");
        String planName = planInfo.getPlanName();
        String startYMD = planInfo.getStartYMD();


        // 画面のタイトルを設定
        actionBar.setTitle(planName);

        // 出発日を表示
        TextView textView = findViewById(R.id.textView_startYMD);
        textView.setText(startYMD);

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

        if (itemID == R.id.menuListOption_Event_List) {
            Intent intent = new Intent(this, EventAddActivity.class);
            intent.putExtra("planDisp",planInfo);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // オプションメニューを作成する
    public boolean onCreateOptionsMenu(Menu menu){
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.menu_options_menu_list, menu);
        // オプションメニュー表示する場合はtrue
        return true;
    }

}
