package rd.slcs.co.jp.showtabi.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import rd.slcs.co.jp.showtabi.R;

public class EventAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventAdd);

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
