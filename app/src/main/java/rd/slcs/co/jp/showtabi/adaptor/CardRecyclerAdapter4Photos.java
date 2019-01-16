package rd.slcs.co.jp.showtabi.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import rd.slcs.co.jp.showtabi.R;

public class CardRecyclerAdapter4Photos extends RecyclerView.Adapter<CardRecyclerAdapter4Photos.ViewHolder> {

    private List<Bitmap> photoList;
    private Context context;

    public CardRecyclerAdapter4Photos(Context context, List<Bitmap> photoList) {
        super();
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {

        vh.imageView_photo.setImageBitmap(photoList.get(position));
        //ToDo

        // タッチ時の処理
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // 長押し時の処理
        vh.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recyclerview_event_photos, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_photo;
        LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            imageView_photo = (ImageView) v.findViewById(R.id.imageView_photo);
            layout = (LinearLayout) v.findViewById(R.id.layout);
        }
    }
}
