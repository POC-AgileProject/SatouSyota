package rd.slcs.co.jp.showtabi.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
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
import rd.slcs.co.jp.showtabi.common.DatePickerDialogFragment;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.UseImagePicker;
import rd.slcs.co.jp.showtabi.common.Util;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.EventDisp;
import rd.slcs.co.jp.showtabi.object.Photo;
import rd.slcs.co.jp.showtabi.object.PlanDisp;
import rd.slcs.co.jp.showtabi.view.CardRecyclerView4EventPhotos;

/**
 * イベントの新規作成アクティビティークラスです。
 */
public class EventAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    /** プランDisp */
    private PlanDisp planInfo;
    /** プランキー */
    private String planKey;
    /** イベント日付 */
    private EditText editEventDate;

    private List<Photo> addPhotos;
    /** 写真登録用イベントキー */
    private String eventKey4Photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);


        // プランキーの値を取得
        Intent intentEventList = getIntent();
        planInfo = (PlanDisp) intentEventList.getSerializableExtra("planDisp");
        planKey = planInfo.getKey();

        // 日付の取得
        editEventDate = findViewById(R.id.editEventDate);
        String eventDate = planInfo.getStartYMD();
        editEventDate.setText(eventDate);

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventAdd);

        // 追加分の写真データリストを初期化
        addPhotos = new ArrayList<>();
    }

    /**
     * オプションメニューを作成する
     *
     * @param menu メニュー
     * @return true（オプションメニュー表示）
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.menu_options_event_add, menu);
        // オプションメニュー表示する場合はtrue
        return true;
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
     * メニューのアイコン押下時
     *
     * @param menuItem メニューアイテム
     * @return true
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Toast toast;

        // 押されたメニューのIDで処理を振り分ける
        switch (menuItem.getItemId()) {

            // 保存ボタン押下時
            case R.id.menuListOption_Event_Add:

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
                    return false;

                }

                Date planStartYmd = Util.convertToDate(planInfo.getStartYMD());
                Date planEndYmd = Util.convertToDate(planInfo.getEndYMD());
                Date eventDate = Util.convertToDate(editEventDate.getText().toString());

                // 日付形式での入力チェック
                if(planStartYmd == null ||
                        planEndYmd == null ||
                        eventDate == null) {
                    Toast.makeText(this, R.string.msg_error_0002, Toast.LENGTH_LONG).show();
                    return false;
                }

                // プラン出発日・最終日とイベント日付の整合性チェック
                // イベント日付がプラン出発日より前
                if (eventDate.compareTo(planStartYmd) < 0) {
                    Toast.makeText(this, R.string.msg_error_0003, Toast.LENGTH_LONG).show();
                    return false;
                }
                // イベント日付がプラン最終日より後
                if (eventDate.compareTo(planEndYmd) > 0) {
                    Toast.makeText(this, R.string.msg_error_0004, Toast.LENGTH_LONG).show();
                    return false;
                }

                // イベント日付（文字列）
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                String sEventDate = fmt.format(eventDate);

                // 日付と時間の連結
                String startTime = sEventDate + editStartTime.getText().toString();
                String endTime = sEventDate + editEndTime.getText().toString();
                
                Event event = new Event();

                // イベントの設定
                event.setPlanKey(planKey);
                event.setEventName(editEventName.getText().toString());
                event.setStartTime(startTime);
                event.setEndTime(endTime);
                event.setMemo(editMemo.getText().toString());
                event.setAddress(editAddress.getText().toString());

                int checkedId = editCategory.getCheckedRadioButtonId();
                if(checkedId != -1) {
                    RadioButton radioButton = (RadioButton)findViewById(checkedId);
                    event.setCategory(radioButton.getText().toString());
                }

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE);

                //push()でキーの自動生成
                DatabaseReference mDatabaseEventKey;
                mDatabaseEventKey = mDatabase.push();
                mDatabaseEventKey.setValue(event);

                // Photoテーブルにデータを保存
                mDatabaseEventKey.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Event event = snapshot.getValue(Event.class);
                        EventDisp eventDisp = new EventDisp(event, snapshot.getKey());
                        eventKey4Photo = eventDisp.getKey();
                        // 写真の追加分を保存
                        savePhoto();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // イベントリスト画面に遷移
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Const.PLANDISP,planInfo);
                startActivity(intent);

                break;

            // 戻るボタン押下時
            case android.R.id.home:
                finish();
                break;

            default:
        }
        return true;
    }

    /**
     * 写真保存処理
     */
    public void savePhoto() {

        // / Firebaseからインスタンスを取得
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE);

        for (Photo photo : addPhotos) {
            photo.setEventKey(eventKey4Photo);
            // データを追加
            mDatabase.push().setValue(photo);
        }
    }

    /**
     * カレンダーアイコン押下時
     * @param v
     */
    public void showDatePickerDialog(View v) {

        Date eventDate = Util.convertToDate(editEventDate.getText().toString());
        if (eventDate == null) {
            DialogFragment newFragment = new DatePickerDialogFragment((Activity)this);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } else {
            DialogFragment newFragment = new DatePickerDialogFragment((Activity)this,eventDate);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String strYear = String.valueOf(year);
        String strMonth = "00";
        String strDate = "00";
        if(monthOfYear + 1 < 10) {
            strMonth = "0" + String.valueOf(monthOfYear + 1);
        }
        else {
            strMonth = String.valueOf(monthOfYear + 1);
        }
        if (dayOfMonth < 10) {
            strDate = "0" + String.valueOf(dayOfMonth);
        }else {
            strDate = String.valueOf(dayOfMonth);
        }

        editEventDate.setText( strYear + strMonth +  strDate);
    }

    /**
     * 時計アイコン押下時
     *
     * @param view
     */
    public void showTimePickDialog(View view) {

        final int textView_id = view.getId();

        //TimePicker選択時の処理
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {

                TextView textView = null;

                if (textView_id == R.id.TimePicker_startTime) {
                    textView = findViewById(R.id.editStartTime);
                } else if (textView_id == R.id.TimePicker_endTime) {
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
