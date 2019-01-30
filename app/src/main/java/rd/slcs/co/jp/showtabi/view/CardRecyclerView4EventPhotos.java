package rd.slcs.co.jp.showtabi.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.adaptor.CardRecyclerAdapter4Photos;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Photo;

public class CardRecyclerView4EventPhotos extends RecyclerView {

    List<Photo> photoList = new ArrayList<>();
    final String eventKey;  // この写真のリストが紐づくイベントのキー
    final Context context;  // この写真のリストが表示される画面

    public CardRecyclerView4EventPhotos(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // 選択されたイベントのイベントキーを取得
        Intent intent = ((Activity) context).getIntent();
        eventKey = intent.getStringExtra(Const.DB_EVENTTABLE_EVENTKEY);

        // DBから写真リストを読み込み＆アダプターにセット
        loadPhotoData();

    }



    /*
        DBから写真情報を読み込む
     */
    public void loadPhotoData() {

        // PhotoListを初期化
        photoList.clear();

        // Firebaseからインスタンスを取得
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE);

        //  Photosテーブルから選択されたイベントに該当する写真情報を抽出
        Query query = mDatabase.orderByChild(Const.DB_PHOTOSTABLE_EVENTKEY).equalTo(eventKey);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // イベントに紐づく写真を取得
                    Photo photoData = dataSnapshot.getValue(Photo.class);
                    photoList.add(photoData);

                }


                // 写真をsortKeyの昇順でソート
                photoList.sort(new Comparator<Photo>() {
                    @Override
                    public int compare(Photo o1, Photo o2) {
                        return o1.getSortKey().compareTo(o2.getSortKey());
                    }
                });

                setRecyclerAdapter(context,photoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    /*
        レイアウト及びアダプターを生成し、紐づける
     */
    public void setRecyclerAdapter(Context context, List<Photo> photoList) {
        setLayoutManager(new GridLayoutManager(context, Const.GRID_SPAN));
        setAdapter(new CardRecyclerAdapter4Photos(context, photoList));
    }



}
