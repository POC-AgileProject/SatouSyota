package rd.slcs.co.jp.showtabi.adaptor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.common.Const;
import rd.slcs.co.jp.showtabi.common.Util;
import rd.slcs.co.jp.showtabi.object.Photo;

public class CardRecyclerAdapter4Photos extends RecyclerView.Adapter<CardRecyclerAdapter4Photos.ViewHolder> {

    private List<Photo> photoList;
    private Context context;

    public CardRecyclerAdapter4Photos(Context context, List<Photo> photoList) {
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

        // 写真1枚あたりのimageViewの大きさを調整
        Point point = Util.getDisplaySize((Activity)context);
        vh.imageView_photo.getLayoutParams().height = point.x / Const.GRID_SPAN;
        vh.imageView_photo.getLayoutParams().width = point.x / Const.GRID_SPAN;

        Photo photodata = photoList.get(position);

        // byte[]→Bitmapに変換
        byte[] decodedString = Base64.decode(photodata.getPhoto().getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // imageViewに画像をバインド
        vh.imageView_photo.setImageBitmap(decodedByte);


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


    public void addPhotoData(Photo photo){

        photoList.add(photo);

    }


}
