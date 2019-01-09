package rd.slcs.co.jp.showtabi.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.activity.EventListActivity;
import rd.slcs.co.jp.showtabi.activity.PlanEditActivity;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.object.PlanDisp;


/**
 */
public class CardRecyclerAdapter4Plan extends RecyclerView.Adapter<CardRecyclerAdapter4Plan.ViewHolder> {
    private List<PlanDisp> planList;
    private Context context;

    public CardRecyclerAdapter4Plan(Context context, List<PlanDisp> planList) {
        super();
        this.planList = planList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        vh.textView_planName.setText(planList.get(position).getPlanName());
        vh.textView_startYMD.setText(planList.get(position).getStartYMD());
        vh.textView_endYMD.setText(planList.get(position).getEndYMD());

        byte[] decodedString = {};

        // プラン画像が設定されている場合
        if(planList.get(position).getIcon() != null){
            // DBから取得した64bitエンコードされている画像ファイルをBitmapにエンコード
            decodedString = Base64.decode(planList.get(position).getIcon(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            vh.imageView_icon.setImageBitmap(decodedByte);
        }

        
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TESTBBB", planList.get(position).getKey());

                Intent intent = new Intent(context, EventListActivity.class);
                intent.putExtra("planDisp", planList.get(position));

                context.startActivity(intent);
            }
        });

        vh.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("TESTAAAA", planList.get(position).getKey());

                Intent intent = new Intent(context, PlanEditActivity.class);
                intent.putExtra(Const.DB_PLANTABLE_PLANKEY,planList.get(position).getKey());
                context.startActivity(intent);
                return true;
            }
        });


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recyclerview_planlist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_planName;
        TextView textView_startYMD;
        TextView textView_endYMD;
        ImageView imageView_icon;
        LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            textView_planName = (TextView) v.findViewById(R.id.textView_planName);
            textView_startYMD = (TextView) v.findViewById(R.id.textView_startYMD);
            textView_endYMD = (TextView) v.findViewById(R.id.textView_endYMD);
            imageView_icon = (ImageView) v.findViewById(R.id.imageView_icon);
            layout = (LinearLayout) v.findViewById(R.id.layout);
        }
    }
}
