package rd.slcs.co.jp.showtabi.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rd.slcs.co.jp.showtabi.adaptor.CardRecyclerAdapter4Event;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Event;
import rd.slcs.co.jp.showtabi.object.EventDisp;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

public class CardRecyclerView4Event extends RecyclerView{
    public CardRecyclerView4Event(final Context context, AttributeSet attrs) {
        super(context, attrs);

        // イベント一覧画面の遷移前で選択されたプランのキーを取得
        Intent intent = ((Activity)context).getIntent();
        PlanDisp planInfo = (PlanDisp)intent.getSerializableExtra(Const.PLANDISP);
        final String planKey = planInfo.getKey();

        // Firebaseからインスタンスを取得
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_EVENTTABLE);

        //  Eventsテーブルから選択されたプランに該当するイベントを抽出
        Query query = mDatabase.orderByChild(Const.DB_EVENTTABLE_PLANKEY).equalTo(planKey);  // 昇順降順のやり方が不明

        // クエリを使用してデータベースの内容を一度だけ取得する
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<EventDisp> eventDispList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    EventDisp eventDisp = new EventDisp(event, dataSnapshot.getKey());
                    eventDispList.add(eventDisp);
                }

                // イベントを開始時間の昇順でソート
                eventDispList.sort(new Comparator<EventDisp>() {
                    @Override
                    public int compare(EventDisp o1, EventDisp o2) {
                        return o1.getStartTime().compareTo(o2.getStartTime());
                    }
                });

                // 保存した情報を用いた描画処理などを記載する。
                setRecyclerAdapter(context,eventDispList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setRecyclerAdapter(Context context, List<EventDisp> eventDispList){
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(new CardRecyclerAdapter4Event(context,eventDispList));
    }
}
