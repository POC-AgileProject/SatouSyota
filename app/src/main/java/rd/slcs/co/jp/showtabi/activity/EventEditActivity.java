package rd.slcs.co.jp.showtabi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.UseImagePicker;
import rd.slcs.co.jp.showtabi.object.Photo;
import rd.slcs.co.jp.showtabi.view.CardRecyclerView4EventPhotos;

public class EventEditActivity extends AppCompatActivity {

    private String eventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventEdit);


        // イベントキーを取得
        Intent intent = getIntent();
        eventKey = intent.getStringExtra(Const.DB_EVENTTABLE_EVENTKEY);

    }


    /*
        遷移先から戻った際の結果受け取り処理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        // ImagePickerからの結果受け取り
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...

            Bitmap bmp;
            String imgPath;

            // Imageからbyteデータ、撮影日時の抽出と、DBへのpush
            for(Image image: images){

                imgPath = image.getPath();
                bmp = BitmapFactory.decodeFile(imgPath);

                // 写真データをbyte[]へ
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                String bmpstr = Base64.encodeToString(bytes, 1);

                // 撮影日時格納用
                String snapData = "";

                // 撮影日時の取得
                try {
                    ExifInterface exifInfo = new ExifInterface(image.getPath());

                    String snapDateformat = exifInfo.getAttribute(ExifInterface.TAG_DATETIME);
                    snapData = snapDateformat.replaceAll(":","");
                    snapData = snapData.substring(0, 8);
                    //Toast.makeText(this, snapDate, Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    Log.d("error","写真にアクセスできませんでした。");
                }


                Photo photo = new Photo();
                photo.setEventKey(eventKey);
                photo.setPhoto(bmpstr);
                photo.setSortKey(snapData);

                // / Firebaseからインスタンスを取得
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE );

                // データを追加
                mDatabase.push().setValue(photo);

            }

            // 写真を再度読み込み
            CardRecyclerView4EventPhotos photoView = findViewById(R.id.CardRecyclerView4Photos);
            photoView.loadPhotoData();


        }

        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here

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

