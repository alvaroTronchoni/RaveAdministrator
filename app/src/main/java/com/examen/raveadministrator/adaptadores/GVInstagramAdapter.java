package com.examen.raveadministrator.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.modelos.InstagramPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GVInstagramAdapter extends ArrayAdapter<InstagramPhoto> {

    private final Context mContext;
    private final int layoutResourceId;
    private ArrayList<InstagramPhoto> mGridData = new ArrayList<>();

    public GVInstagramAdapter(Context mContext, ArrayList<InstagramPhoto> mGridData) {
        super(mContext, R.layout.elemento_instagram_photo, mGridData);
        this.layoutResourceId = R.layout.elemento_instagram_photo;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<InstagramPhoto> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.photo);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final InstagramPhoto item = mGridData.get(position);

        Picasso.with(mContext).load(item.getImageUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                mContext.startActivity(instagramIntent);
            }
        });

        return row;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}