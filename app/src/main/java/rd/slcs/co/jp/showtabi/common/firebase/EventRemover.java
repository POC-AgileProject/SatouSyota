package rd.slcs.co.jp.showtabi.common.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Photo;
import rd.slcs.co.jp.showtabi.object.Plan;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

public class EventRemover {
    /** 削除したいイベントキー */
    private final String eventKey;

    /**
     * constructor.
     * @param pEventKey
     */
    public EventRemover(String pEventKey) {
        eventKey = pEventKey;
    }

    /**
     * remove event and related photos.
     * removing photo bia calling private method
     */
    public void removeEvent(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE + "/" + eventKey);
        mDatabase.removeValue();

        removePhotosRelatedEventKey();
    }

    /**
     * remove
     */
    private void removePhotosRelatedEventKey() {
        // Firebaseから写真テーブルのインスタンスを取得
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE);

        Query query = mDatabase.orderByChild(Const.DB_PHOTOSTABLE_EVENTKEY).equalTo(eventKey);
        // クエリを使用してデータベースの内容を一度だけ取得する
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<PlanDisp> planDispList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Photo photo = dataSnapshot.getValue(Photo.class);
                    String photoKey = dataSnapshot.getKey();

                    if (eventKey.equals(photo.getEventKey())) {
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PHOTOSTABLE + "/" + photoKey);
                        mDatabase.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
