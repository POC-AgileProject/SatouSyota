package rd.slcs.co.jp.showtabi.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rd.slcs.co.jp.showtabi.adaptor.CardRecyclerAdapter4Photos;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;

public class CardRecyclerView4EventPhotos extends RecyclerView {

    List<Bitmap> photoList = new ArrayList<>();


    public CardRecyclerView4EventPhotos(final Context context, AttributeSet attrs) {
        super(context, attrs);

        // 選択されたイベントのイベントキーを取得
        Intent intent = ((Activity) context).getIntent();
        final String eventKey = intent.getStringExtra(Const.DB_EVENTTABLE_EVENTKEY);

        // Firebaseからインスタンスを取得
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE);

        //  Photosテーブルから選択されたイベントに該当する写真情報を抽出
        Query query = mDatabase.orderByChild(Const.DB_PHOTOSTABLE_EVENTKEY).equalTo(eventKey);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseDB_Photos", snapshot.toString());

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // イベントに紐づく写真を取得
                    String photoData = (String)dataSnapshot.child(Const.DB_PHOTOSTABLE_PHOTO).getValue();

                    Log.d("photoData=", photoData);

                    byte[] decodedString = photoData.getBytes();
                    // byte[] → Bitmap　に変換
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    photoList.add(decodedByte);

                }

                // TODO: 写真をsortKeyを基準にソート


                setRecyclerAdapter(context, photoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setRecyclerAdapter(Context context, List<Bitmap> photoList) {
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(new CardRecyclerAdapter4Photos(context, photoList));
    }


    /*
        BMPのリストを受け取り配列に追加。及びDBに追加する。
     */
    public static void addPhotoImage(){



    }


}
