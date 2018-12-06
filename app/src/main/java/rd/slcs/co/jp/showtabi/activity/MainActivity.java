package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rd.slcs.co.jp.showtabi.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){

            public void run(){
                Intent intent = new Intent(MainActivity.this, PlanListActivity.class);
                startActivity(intent);

                MainActivity.this.finish();

            }

        }, 2000);

    }
}
