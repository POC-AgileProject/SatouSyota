package rd.slcs.co.jp.showtabi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import rd.slcs.co.jp.showtabi.adaptor.CardRecyclerAdapter4Photos;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.UseImagePicker;
import rd.slcs.co.jp.showtabi.object.Photo;
import rd.slcs.co.jp.showtabi.view.CardRecyclerView4EventPhotos;

import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.EventDisp;

public class EventEditActivity extends AppCompatActivity {

    /** イベントDisp */
    private EventDisp eventDisp;

    /** イベントキー */
    private String eventKey;

    private List<Photo> addPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        // イベントDispを取得
        Intent intentEventList = getIntent();
        eventDisp = (EventDisp) intentEventList.getSerializableExtra(Const.EVENTDISP);

        // イベントキーの値を取得
        eventKey = eventDisp.getKey();

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventEdit);


        // イベントキーを取得
        Intent intent = getIntent();
        eventKey = intent.getStringExtra(Const.DB_EVENTTABLE_EVENTKEY);

        // 追加分の写真データリストを初期化
        addPhotos = new ArrayList<>();

    }

    /**
     * 保存ボタン押下時処理
     *
     * @param v View
     */
    public void onClickSaveButton(View v) {

        // 画面の値を取得
        EditText editEventName = findViewById(R.id.editEventName);
        EditText editEventDate = findViewById(R.id.editEventDate);
        EditText editStartTime = findViewById(R.id.editStartTime);
        EditText editEndTime = findViewById(R.id.editEndTime);
        RadioGroup editCategory = findViewById(R.id.editCategory);
        EditText editMemo = findViewById(R.id.editMemo);
        EditText editAddress = findViewById(R.id.editAddress);

        //TODO:開始日と終了日の前後チェック
        // 入力チェック
        if ("".equals(editEventName.getText().toString())
                || "".equals(editEventDate.getText().toString())
                || "".equals(editStartTime.getText().toString())) {

            Toast.makeText(this, R.string.msg_error_0001, Toast.LENGTH_LONG).show();

        } else {

            // 日付と時間の連結
            String startTime = editEventDate.getText().toString() + editStartTime.getText().toString();
            String endTime = "";
            // 終了時間が入力されている場合
            if (!"".equals(editEndTime.getText().toString())) {
                endTime = editEventDate.getText().toString() + editEndTime.getText().toString();
            }

            Event event = new Event();

            // イベントの設定
            event.setPlanKey(eventDisp.getPlanKey());
            event.setEventName(editEventName.getText().toString());
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            event.setMemo(editMemo.getText().toString());
            event.setAddress(editAddress.getText().toString());

            int checkedId = editCategory.getCheckedRadioButtonId();
            if (checkedId != -1) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                event.setCategory(radioButton.getText().toString());
            }

            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE + "/" + eventKey);

            //push()でキーの自動生成
            mDatabase.setValue(event);

            finish();
        }


    }

    /**
     * 削除ボタン押下時処理
     *
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


        // ImagePickerからの結果受け取り
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...

            Bitmap bmp;
            String imgPath;

            // Imageからbyteデータ、撮影日時の抽出と、DBへのpush
            for (Image image : images) {

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
                    snapData = snapDateformat.replaceAll(":", "");
                    snapData = snapData.substring(0, 8);
                    //Toast.makeText(this, snapDate, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.d("error", "写真にアクセスできませんでした。");
                } catch (NullPointerException e) {
                    Log.d("error", "写真に撮影日がありません。");
                }


                Photo photo = new Photo();
                photo.setEventKey(eventKey);
                photo.setPhoto(bmpstr);
                photo.setSortKey(snapData);

                addPhotos.add(photo);

                //ToDo : 保存ボタン押下後に移動
//                // / Firebaseからインスタンスを取得
//                DatabaseReference mDatabase;
//                mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE );
//
//
//                // データを追加
//                mDatabase.push().setValue(photo);

                // 写真をViewに追加
                CardRecyclerView4EventPhotos photoView = findViewById(R.id.CardRecyclerView4Photos);
                //photoView.loadPhotoData();
                CardRecyclerAdapter4Photos photoAdapter = (CardRecyclerAdapter4Photos) photoView.getAdapter();
                photoAdapter.addPhotoData(photo);
                photoAdapter.notifyDataSetChanged();

            }


        }

        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here

    }


    /*
        写真を追加したい場合に押下される
     */
    public void onClickPhotoIcon(View view) {

        //ImagePickerを起動
        UseImagePicker.start(this);

    }


    /*
        メニューのアイコンが押下された場合の処理を行います。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();

        if (itemID == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

