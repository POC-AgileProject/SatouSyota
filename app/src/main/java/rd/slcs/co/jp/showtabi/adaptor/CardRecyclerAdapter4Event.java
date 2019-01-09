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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.activity.EventEditActivity;
import rd.slcs.co.jp.showtabi.activity.EventReferenceActivity;
import rd.slcs.co.jp.showtabi.common.Const;
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
        vh.textView_startTime.setText(eventList.get(position).getStartTime());
        vh.textView_endTime.setText(eventList.get(position).getEndTime());
        //ToDo

        byte[] decodedString = {};

        // イベント画像が設定されている場合
        if(eventList.get(position).getAddress() != null){
            // DBから取得した64bitエンコードされている画像ファイルをBitmapにエンコード
            decodedString = Base64.decode(eventList.get(position).getAddress(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //vh.imageView_icon.setImageBitmap(decodedByte);
        }


        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TESTCCC", eventList.get(position).getKey());

                Intent intent = new Intent(context, EventReferenceActivity.class);
                intent.putExtra(Const.DB_EVENTTABLE_EVENTKEY,eventList.get(position).getKey());
                context.startActivity(intent);
            }
        });

        vh.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("TESTDDDD", eventList.get(position).getKey());

                Intent intent = new Intent(context, EventEditActivity.class);
                intent.putExtra(Const.DB_EVENTTABLE_EVENTKEY,eventList.get(position).getKey());
                context.startActivity(intent);
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
        //ImageView imageView_icon;
        LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            textView_eventName = (TextView) v.findViewById(R.id.textView_eventName);
            textView_startTime = (TextView) v.findViewById(R.id.textView_startTime);
            textView_endTime = (TextView) v.findViewById(R.id.textView_endTime);
            //imageView_icon = (ImageView) v.findViewById(R.id.imageView_icon);
            layout = (LinearLayout) v.findViewById(R.id.layout);
        }
    }
}
