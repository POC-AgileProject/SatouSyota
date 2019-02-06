package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;

public class PlanListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

        // アクションバーの取得
        ActionBar actionBar = getSupportActionBar();
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_planList);

    }

    /*
    メニューのアイコンが押下された場合の処理を行います。
 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int itemID = item.getItemId();

        // 新規登録ボタンが押下された場合
        if (itemID == R.id.menuListOption_Plan_List) {
            Intent intent = new Intent(this, PlanAddActivity.class);  //インテントの作成
            startActivity(intent); //画面遷移
        }

        return super.onOptionsItemSelected(item);
    }

    // オプションメニューを作成する
    public boolean onCreateOptionsMenu(Menu menu){
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.menu_options_menu_plan_list, menu);
        // オプションメニュー表示する場合はtrue
        return true;
    }

}
