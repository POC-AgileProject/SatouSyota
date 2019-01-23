package rd.slcs.co.jp.showtabi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.UseImagePicker;

public class EventEditActivity extends AppCompatActivity {

    /** イベントキー */
    private String eventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        // イベントキーの値を取得
        Intent intentEventList = getIntent();
        eventKey = (String)intentEventList.getSerializableExtra(Const.DB_EVENTTABLE_EVENTKEY);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventEdit);

    }

    /**
     * 削除ボタン押下時処理
     * @param v View
     */
    public void onClickDelButton(View v) {

        new AlertDialog.Builder(EventEditActivity.this)
                .setTitle(R.string.alertDialog_title)
                .setMessage(R.string.msg_warning_0001)
                .setPositiveButton(
                        R.string.alertDialog_positiveButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE + "/" + eventKey);
                                mDatabase.removeValue();

                                Intent intent = new Intent(EventEditActivity.this, EventListActivity.class);
                                startActivity(intent);

                            }
                        })
                .setNegativeButton(
                        R.string.alertDialog_negativeButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .show();
    }

    /*
        遷移先から戻った際の結果受け取り処理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
        // ImagePickerからの結果受け取り
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...

            List<String> photoList = new ArrayList<>();

            Bitmap bmp;
            String imgPath;

            //　TODO:Image→BMPの変換
            for(Image image: images){

                imgPath = image.getPath();
                bmp = BitmapFactory.decodeFile(imgPath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();

                String bmpstr = new String(bytes);
                photoList.add(bmpstr);

            }

            // recyclerViewのクラスへBMPリストを渡す。




        }

        */

        //super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here

    }




    /*
        写真を追加したい場合に押下される
     */
    public void onClickPhotoIcon(View view){

        //ImagePickerを起動
        UseImagePicker.start(this);

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

