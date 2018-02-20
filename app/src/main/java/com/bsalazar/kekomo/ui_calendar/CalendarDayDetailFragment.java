package com.bsalazar.kekomo.ui_calendar;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.controllers.EventsController;
import com.bsalazar.kekomo.bbdd_room.entities.Dish;
import com.bsalazar.kekomo.bbdd_room.entities.Event;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.FileSystem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 20/06/2017.
 */

public class CalendarDayDetailFragment extends Fragment {

    private ViewGroup rootView;
    private Context mContext;

    private LinearLayout container_layout, empty;
    private LinearLayout breakfast_container, lunch_container, dinner_container;
    private ImageView breakfast_image, lunch_image, dinner_image;
    private TextView breakfast, lunch, dinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.calendar_day_detail, container, false);
        mContext = getActivity().getApplicationContext();

        container_layout = (LinearLayout) rootView.findViewById(R.id.container);
        empty = (LinearLayout) rootView.findViewById(R.id.empty);

        breakfast_container = (LinearLayout) rootView.findViewById(R.id.breakfast_container);
        lunch_container = (LinearLayout) rootView.findViewById(R.id.lunch_container);
        dinner_container = (LinearLayout) rootView.findViewById(R.id.dinner_container);

        breakfast_image = (ImageView) rootView.findViewById(R.id.breakfast_image);
        lunch_image = (ImageView) rootView.findViewById(R.id.lunch_image);
        dinner_image = (ImageView) rootView.findViewById(R.id.dinner_image);

        breakfast = (TextView) rootView.findViewById(R.id.breakfast);
        lunch = (TextView) rootView.findViewById(R.id.lunch);
        dinner = (TextView) rootView.findViewById(R.id.dinner);

        String dt = getArguments().getString("DATE", "");

        ArrayList<Event> events = new EventsController().getForDate(dt);

        if(events.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            container_layout.setVisibility(View.GONE);
        }else {
            empty.setVisibility(View.GONE);
            container_layout.setVisibility(View.VISIBLE);
        }
        for(Event event : events){
            Dish dish = new DishesController().getByID(event.getDishId());
            switch (event.getType()){
                case Constants.DISH_TYPE_BREAKFAST:
                    breakfast_container.setVisibility(View.VISIBLE);
                    breakfast.setText(dish.getName());

                    Glide.with(mContext)
                            .load(FileSystem.getInstance(mContext).IMAGES_PATH + dish.getImage())
                            .asBitmap()
                            .into(breakfast_image);
                    break;

                case Constants.DISH_TYPE_LUNCH:
                    lunch_container.setVisibility(View.VISIBLE);
                    lunch.setText(dish.getName());

                    Glide.with(mContext)
                            .load(FileSystem.getInstance(mContext).IMAGES_PATH + dish.getImage())
                            .asBitmap()
                            .into(lunch_image);
                    break;

                case Constants.DISH_TYPE_DINNER:
                    dinner_container.setVisibility(View.VISIBLE);
                    dinner.setText(dish.getName());

                    Glide.with(mContext)
                            .load(FileSystem.getInstance(mContext).IMAGES_PATH + dish.getImage())
                            .asBitmap()
                            .into(dinner_image);
                    break;
            }
        }

        return rootView;
    }
}
