package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.adaptor.CardRecyclerAdapter4Event;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.Util;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.EventDisp;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

import static rd.slcs.co.jp.showtabi.R.id.CardRecyclerView4Event;

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

    /*
        画面の初期設定をおこないます。
     */
    private void initScreen(){

        setContentView(R.layout.activity_event_list);

        Date startYMD = Util.convertToDate(planInfo.getStartYMD());
        // 出発日を表示
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
        TextView textView = findViewById(R.id.textView_startYMD);
        textView.setText(fmt.format(startYMD));



    }
}
