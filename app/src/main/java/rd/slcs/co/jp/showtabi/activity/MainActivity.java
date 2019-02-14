package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import rd.slcs.co.jp.showtabi.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView topLogo = findViewById(R.id.topLogo);
        fadein(topLogo);

        new Handler().postDelayed(new Runnable(){

            public void run(){
                Intent intent = new Intent(MainActivity.this, PlanListActivity.class);
                startActivity(intent);

                MainActivity.this.finish();

            }

        }, 2000);

    }

    private void fadein(ImageView image){
        // 透明度を0から1に変化
        AlphaAnimation alphaFadeIn = new AlphaAnimation(0.0f, 1.0f);
        // animation時間 msec
        alphaFadeIn.setDuration(1500);
        // animationが終わったそのまま表示にする
        alphaFadeIn.setFillAfter(true);

        image.startAnimation(alphaFadeIn);
    }
}
