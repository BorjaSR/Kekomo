package com.bsalazar.kekomo.general;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bsalazar.kekomo.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 27/06/2017.
 */

public class GridImagesAdapter extends ArrayAdapter<Integer> {

    private Context mContext;
    private ArrayList<Integer> images;

    public GridImagesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Integer> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.images = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_image_item, parent, false);
        }

        final ImageView image = (ImageView) convertView.findViewById(R.id.image);

        Glide.with(mContext)
                .load(images.get(position))
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GaleryActivity) mContext).setGridResult(images.get(position));
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
