package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import rd.slcs.co.jp.showtabi.R;

public class PlanListActivity extends AppCompatActivity
        implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

        // アクションバーの取得
        ActionBar actionBar = getSupportActionBar();
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_planList);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PlanAddActivity.class);  //インテントの作成
        startActivity(intent);                                                      //画面遷移
    }
}
