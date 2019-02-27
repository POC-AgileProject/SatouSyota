package rd.slcs.co.jp.showtabi.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.activity.EventListActivity;
import rd.slcs.co.jp.showtabi.activity.PlanEditActivity;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Util;
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

        Date startYMD = Util.convertToDate(planList.get(position).getStartYMD());
        Date endYMD = Util.convertToDate(planList.get(position).getEndYMD());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/M/d'('E')'");

        vh.textView_planName.setText(planList.get(position).getPlanName());
        vh.textView_startYMD.setText(fmt.format(startYMD));
        vh.textView_endYMD.setText(fmt.format(endYMD));

        byte[] decodedString = {};

        String planIcon = planList.get(position).getIcon();
        // プラン画像が設定されている場合
        if(planIcon != null && !"".equals(planIcon)){
            // DBから取得した64bitエンコードされている画像ファイルをBitmapにエンコード
            decodedString = Base64.decode(planList.get(position).getIcon(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            vh.imageView_icon.setImageBitmap(decodedByte);
        }

        // タッチ時の処理
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(context, EventListActivity.class);
                intent.putExtra(Const.PLANDISP, planList.get(position));
                ((Activity)context).startActivityForResult(intent, Const.SCREEN_EVENTLIST);
            }
        });

        // 長押し時の処理
        vh.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
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
