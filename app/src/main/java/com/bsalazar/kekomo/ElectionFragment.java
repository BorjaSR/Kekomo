package com.bsalazar.kekomo;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.kekomo.data.LocalDataSource;
import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.data.entities.Event;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.FileSystem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bsalazar on 20/06/2017.
 */

public class ElectionFragment extends Fragment implements View.OnClickListener{

    private ViewGroup rootView;
    private Context mContext;
    private ArrayList<Integer> dishes;
    private Integer actual_dish = 0;

    private ImageView dish_image;
    private TextView dihs_name, no, yes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.election_fragment, container, false);
        mContext = getActivity().getApplicationContext();

        dishes = getArguments().getIntegerArrayList("dishes");

        dish_image = (ImageView) rootView.findViewById(R.id.dish_image);
        dihs_name = (TextView) rootView.findViewById(R.id.dihs_name);
        no = (TextView) rootView.findViewById(R.id.no);
        yes = (TextView) rootView.findViewById(R.id.yes);

        no.setOnClickListener(this);
        yes.setOnClickListener(this);

        configureLayout();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.no:
                actual_dish++;
                if (actual_dish == dishes.size()) actual_dish = 0;
                configureLayout();
                break;
            case R.id.yes:

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                Dish dish = LocalDataSource.getInstance(mContext).getDishByID(dishes.get(actual_dish));
                Event event = new Event();
                event.setDishID(dish.getId());
                event.setDate(dateFormat.format(new Date()));

                if (new Date().getHours() <= Constants.LUNCH_TIME)
                    event.setType(Constants.DISH_TYPE_LUNCH);
                else
                    event.setType(Constants.DISH_TYPE_DINNER);

                LocalDataSource.getInstance(mContext).saveEvent(event);

                ((MainActivity) getActivity()).configUI();
                getActivity().onBackPressed();
                break;
        }
    }

    private void configureLayout() {

        Dish dish = LocalDataSource.getInstance(mContext).getDishByID(dishes.get(actual_dish));

        Glide.with(mContext)
                .load(FileSystem.getInstance(mContext).IMAGES_PATH + dish.getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(dish_image);

        dihs_name.setText(dish.getName());
    }

}
