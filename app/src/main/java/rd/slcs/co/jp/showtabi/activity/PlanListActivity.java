package rd.slcs.co.jp.showtabi.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Env;
import rd.slcs.co.jp.showtabi.object.Plan;

public class PlanListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

 //       FirebaseApp.initializeApp(this);

        // plansテーブル配下のデータを参照するためのリファレンスを取得する
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(Env.DB_USERNAME + "/" + Const.DB_PLANTABLE);

        //  plansテーブルのstartYMDの昇順にソートするクエリを作成
        Query query = mDatabase.orderByChild(Const.DB_PLANTABLE_STARTYMD);
        // クエリを使用してデータベースの内容を一度だけ取得する
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseDB",snapshot.toString());
//                List<JSONObject> plans = new ArrayList<>();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Plan plan = dataSnapshot.getValue(Plan.class);
//                    JSONObject json = new JSONObject();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
