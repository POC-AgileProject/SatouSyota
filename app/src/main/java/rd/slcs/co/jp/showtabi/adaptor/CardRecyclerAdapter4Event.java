package rd.slcs.co.jp.showtabi.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import rd.slcs.co.jp.showtabi.activity.EventEditActivity;
import rd.slcs.co.jp.showtabi.activity.EventReferenceActivity;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Util;
import rd.slcs.co.jp.showtabi.object.EventDisp;


/**
 */
public class CardRecyclerAdapter4Event extends RecyclerView.Adapter<CardRecyclerAdapter4Event.ViewHolder> {

    private List<EventDisp> eventList;
    private Context context;

    public CardRecyclerAdapter4Event(Context context, List<EventDisp> EventList) {
        super();
        this.eventList = EventList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {

        vh.textView_eventName.setText(eventList.get(position).getEventName());
        
        // イベントの開始時間と終了時間をDate型に変換
        Date startDate = Util.convertToDate(eventList.get(position).getStartTime());
        Date endDate = Util.convertToDate(eventList.get(position).getEndTime());

        // 表示形式にフォーマット
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        vh.textView_startTime.setText(fmt.format(startDate));
        if(endDate != null) {
            vh.textView_endTime.setText(fmt.format(endDate));
        }

        // イベントアイコンを設定
        String category = eventList.get(position).getCategory();

        // データ定義を考慮すればこのif文で囲む必要はない。
        if(Const.categoryToIconMap.containsKey(category)) {
            vh.imageView_category.setImageResource(Const.categoryToIconMap.get(category));
        }


        // メモアイコンを設定
        if(!"".equals(eventList.get(position).getMemo())) {
            vh.imageView_memo.setImageResource(R.drawable.ic_insert_comment_24dp);
        }

        // ToDO:URLが実装されたら
        /*
        if(!"".equals(eventList.get(position).getLink())) {
            vh.imageView_link.setImageResource(R.drawable.ic_insert_link_24dp);
        }
        */


        // タッチ時の処理
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventReferenceActivity.class);
                intent.putExtra(Const.EVENTDISP, eventList.get(position));
                ((Activity)context).startActivityForResult(intent, Const.SCREEN_EVENTREFERENCE);
            }
        });

        // 長押し時の処理
        vh.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, EventEditActivity.class);
                intent.putExtra(Const.EVENTDISP, eventList.get(position));
                ((Activity)context).startActivityForResult(intent, Const.SCREEN_EVENTEDIT);
                return true;
            }
        });

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recyclerview_eventlist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_eventName;
        TextView textView_startTime;
        TextView textView_endTime;
        ImageView imageView_category;
        ImageView imageView_memo;
        ImageView imageView_link;
        LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            textView_eventName = (TextView) v.findViewById(R.id.textView_eventName);
            textView_startTime = (TextView) v.findViewById(R.id.textView_startTime);
            textView_endTime = (TextView) v.findViewById(R.id.textView_endTime);
            imageView_category = (ImageView) v.findViewById(R.id.imageView_category);
            imageView_memo = (ImageView) v.findViewById(R.id.imageView_memo);
            imageView_link = (ImageView) v.findViewById(R.id.imageView_link);
            layout = (LinearLayout) v.findViewById(R.id.layout);
        }
    }
}
