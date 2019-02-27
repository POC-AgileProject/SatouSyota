package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Util;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

public class EventListActivity extends AppCompatActivity {

    private PlanDisp planInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        planInfo = (PlanDisp)intent.getSerializableExtra(Const.PLANDISP);
        String planName = planInfo.getPlanName();

        initScreen();

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 画面のタイトルを設定
        actionBar.setTitle(planName);

    }

    /*
        メニューのアイコンが押下された場合の処理を行います。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int itemID = item.getItemId();

        // 戻るアイコンが押下された場合
        if(itemID == android.R.id.home){
            finish();
        }

        // 新規登録ボタンが押下された場合
        if (itemID == R.id.menuListOption_Event_List) {
            Intent intent = new Intent(this, EventAddActivity.class);
            intent.putExtra(Const.PLANDISP, planInfo);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            initScreen();

    }

    /**
     *  画面の初期設定をおこないます。
     */
    private void initScreen(){

        setContentView(R.layout.activity_event_list);

        Date startYMD = Util.convertToDate(planInfo.getStartYMD());
        Date endYMD = Util.convertToDate(planInfo.getEndYMD());
        // 「出発日～最終日」を表示
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/M/d'('E')'");
        TextView textView = findViewById(R.id.textView_planYMD);
        String planYmd = fmt.format(startYMD) + Const.PERIOD + fmt.format(endYMD);
        textView.setText(planYmd);
    }
}
