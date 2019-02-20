package rd.slcs.co.jp.showtabi.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.adaptor.CardRecyclerAdapter4Photos;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.UseImagePicker;
import rd.slcs.co.jp.showtabi.common.Util;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.EventDisp;
import rd.slcs.co.jp.showtabi.object.Photo;
import rd.slcs.co.jp.showtabi.object.Plan;
import rd.slcs.co.jp.showtabi.object.PlanDisp;
import rd.slcs.co.jp.showtabi.view.CardRecyclerView4EventPhotos;

public class EventEditActivity extends AppCompatActivity {

    private String eventName;
    private String eventDate;
    private String startTime;
    private String endTime;
    private String eventCategory;
    private String eventMemo;
    private String eventAddress;

    /** イベントDisp */
    private EventDisp eventDisp;

    /** イベントキー */
    private String eventKey;

    /** 出発日 */
    private String planStartYmd;
    /** 最終日 */
    private String planEndYmd;

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

        // 編集画面の各値を設定
        EditText viewEventName = findViewById(R.id.editEventName);
        eventName = eventDisp.getEventName();
        viewEventName.setText(eventName);

        EditText viewEventDate = findViewById(R.id.editEventDate);
        eventDate = eventDisp.getStartTime().substring(0, 8);
        viewEventDate.setText(eventDate);

        EditText viewStartTime = findViewById(R.id.editStartTime);
        startTime = eventDisp.getStartTime().substring(8);
        viewStartTime.setText(startTime);

        EditText viewEndTime = findViewById(R.id.editEndTime);
        endTime = eventDisp.getEndTime().substring(8);
        viewEndTime.setText(endTime);

        RadioGroup viewEventCategory = findViewById(R.id.editCategory);
        eventCategory = eventDisp.getCategory();

        viewEventCategory.check(Const.categoryToRadioButtonId.get(eventCategory));

        EditText viewEventMemo = findViewById(R.id.editMemo);
        eventMemo = eventDisp.getMemo();
        viewEventMemo.setText(eventMemo);

        EditText viewEventAddress = findViewById(R.id.editAddress);
        eventAddress = eventDisp.getAddress();
        viewEventAddress.setText(eventAddress);

        // Firebaseからインスタンスを取得
        DatabaseReference mDatabasePlan;
        mDatabasePlan = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE  + "/" + eventDisp.getPlanKey());

        // クエリを使用してデータベースの内容を一度だけ取得する
        mDatabasePlan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Plan plan = snapshot.getValue(Plan.class);
                PlanDisp planDisp = new PlanDisp(plan, snapshot.getKey());

                planStartYmd = planDisp.getStartYMD();
                planEndYmd = planDisp.getEndYMD();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventEdit);


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

        // 入力チェック
        if ("".equals(editEventName.getText().toString())
                || "".equals(editEventDate.getText().toString())
                || "".equals(editStartTime.getText().toString())
                || "".equals(editEndTime.getText().toString())) {

            Toast.makeText(this, R.string.msg_error_0001, Toast.LENGTH_LONG).show();

            return;
        }

        Date planStartYmd = Util.convertToDate(this.planStartYmd);
        Date planEndYmd = Util.convertToDate(this.planEndYmd);
        Date eventDate = Util.convertToDate(editEventDate.getText().toString());

        // 日付形式での入力チェック
        if(planStartYmd == null ||
                planEndYmd == null ||
                eventDate == null) {
            Toast.makeText(this, R.string.msg_error_0002, Toast.LENGTH_LONG).show();
            return;
        }

        // プラン出発日・最終日とイベント日付の整合性チェック
        // イベント日付がプラン出発日より前
        if (eventDate.compareTo(planStartYmd) < 0) {
            Toast.makeText(this, R.string.msg_error_0003, Toast.LENGTH_LONG).show();
            return;
        }
        // イベント日付がプラン最終日より後
        if (eventDate.compareTo(planEndYmd) > 0) {
            Toast.makeText(this, R.string.msg_error_0004, Toast.LENGTH_LONG).show();
            return;
        }

        // イベント日付（文字列）
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String sEventDate = fmt.format(eventDate);

        // 日付と時間の連結
        String startTime = sEventDate + editStartTime.getText().toString();
        String endTime = sEventDate + editEndTime.getText().toString();

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

        // イベントデータを再登録
        mDatabase.setValue(event);

        // 写真の追加分を保存
        savePhoto();

        Intent intent = new Intent();
        EventDisp eventDisp = new EventDisp(event, eventKey);
        intent.putExtra("eventDisp", eventDisp);
        intent.putExtra("hanteiKey", Const.HANTEIKEY_SAVE);
        setResult(RESULT_OK, intent);

        finish();
    }

    /**
     * 削除ボタン押下時処理
     *
     * @param v View
     */
    public void onClickDelButton(View v) {

        new AlertDialog.Builder(EventEditActivity.this)
                .setTitle(R.string.alertDialog_title)
                .setMessage(R.string.msg_warning_0002)
                .setPositiveButton(
                        R.string.alertDialog_positiveButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE + "/" + eventKey);
                                mDatabase.removeValue();

                                Intent intent = new Intent();
                                intent.putExtra("hanteiKey",Const.HANTEIKEY_DEL);
                                setResult(RESULT_OK, intent);
                                
                                finish();
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

    /**
     * 遷移先から戻った際の結果受け取り処理
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


    /**
     * 写真を追加したい場合に押下される
     */
    public void onClickPhotoIcon(View view) {

        //ImagePickerを起動
        UseImagePicker.start(this);

    }


    /**
     * メニューのアイコンが押下された場合の処理を行います。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();

        if(itemID == android.R.id.home){
            setResult(RESULT_CANCELED);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void savePhoto() {

        // / Firebaseからインスタンスを取得
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE);


        for (Photo photo : addPhotos) {

            // データを追加
            mDatabase.push().setValue(photo);

        }

    }



    /**
     * 時計アイコン押下時
     * @param view
     */
    public void showTimePickDialog(View view){

        final int textView_id = view.getId();

    //TimePicker選択時の処理
    TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {

            TextView textView = null;

            if (textView_id == R.id.TimePicker_startTime) {
                textView = findViewById(R.id.editStartTime);
            } else if (textView_id == R.id.TimePicker_endTime){
                textView = findViewById(R.id.editEndTime);
            }

            String strHour = String.format("%02d", hour);
            String strMinute = String.format("%02d", minute);

            textView.setText(strHour + strMinute);
        }
    };

        TimePickerDialog dialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                                                        listener, Const.DEFAULT_HOUR, Const.DEFAULT_MINUTE, true);
        dialog.show();

    }


}

