package rd.slcs.co.jp.showtabi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.common.UseImagePicker;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.EventDisp;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

public class EventEditActivity extends AppCompatActivity {

    private PlanDisp planInfo;
    private String planKey;
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

    /** プラン情報　出発日 */
    private String planStartDay;
    /** プラン情報　最終日 */
    private String planEndDay;

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

        // 戻るメニューの有効化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 画面のタイトルを設定
        actionBar.setTitle(R.string.title_eventEdit);

    }
    /**
     * 保存ボタン押下時処理
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
            String endTime ="";
            // 終了時間が入力されている場合
            if(!"".equals(editEndTime.getText().toString())) {
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
            if(checkedId != -1) {
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
                event.setCategory(radioButton.getText().toString());
            }

            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE + "/" + eventKey);

            //push()でキーの自動生成
            mDatabase.setValue(event);

            Intent intent = new Intent();
            EventDisp eventDisp = new EventDisp(event, eventKey);
            intent.putExtra("eventDisp",eventDisp);
            intent.putExtra("hanteiKey", Const.HANTEIKEY_SAVE);
            setResult(RESULT_OK, intent);

            finish();
        }




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
            setResult(RESULT_CANCELED);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

