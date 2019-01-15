package rd.slcs.co.jp.showtabi.view;

import android.content.Context;
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
import java.util.List;

import rd.slcs.co.jp.showtabi.adaptor.CardRecyclerAdapter4Plan;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Plan;
import rd.slcs.co.jp.showtabi.object.PlanDisp;

public class CardRecyclerView4Plan extends RecyclerView{
    public CardRecyclerView4Plan(final Context context, AttributeSet attrs) {
        super(context, attrs);

        // Firebaseからインスタンスを取得
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE);

        //  plansテーブルのstartYMDの昇順にソートするクエリを作成
        Query query = mDatabase.orderByChild(Const.DB_PLANTABLE_STARTYMD);
        // クエリを使用してデータベースの内容を一度だけ取得する
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<PlanDisp> planDispList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Plan plan = dataSnapshot.getValue(Plan.class);
                    PlanDisp planDisp = new PlanDisp(plan, dataSnapshot.getKey());
                    planDispList.add(planDisp);
                }

                //TODO: プランを降順にソート

                // 保存した情報を用いた描画処理などを記載する。
                setRecyclerAdapter(context,planDispList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setRecyclerAdapter(Context context, List<PlanDisp> planDispList){
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(new CardRecyclerAdapter4Plan(context,planDispList));
}
}
