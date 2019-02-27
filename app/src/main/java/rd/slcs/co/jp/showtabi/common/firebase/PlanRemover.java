package rd.slcs.co.jp.showtabi.common.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.Photo;

public class PlanRemover {

    private final String planKey;

    public PlanRemover(String pPlanKey) {
        planKey = pPlanKey;
    }


    public void removePlan(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE + "/" + planKey);
        mDatabase.removeValue();

        removeEventsRelatedPlanKey();
    }

    /**
     * remove event related to Plan key.
     */
    private void removeEventsRelatedPlanKey() {
        // Firebaseからイベントテーブルのインスタンスを取得
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE);

        // Planキーに合致するクエリを取得
        Query query = mDatabase.orderByChild(Const.DB_EVENTTABLE_PLANKEY).equalTo(planKey);
        // クエリを使用してデータベースの内容を一度だけ取得する
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    String eventKey = dataSnapshot.getKey();

                    // イベントキーが合致するイベントについて削除処理を実行
                    if (planKey.equals(event.getPlanKey())) {
                        EventRemover eventRemover = new EventRemover(eventKey);
                        eventRemover.removeEvent();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
