package com.bsalazar.kekomo.ui_calendar;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.kekomo.ElectionActivity;
import com.bsalazar.kekomo.MainActivity;
import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.data.LocalDataSource;
import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.data.entities.Event;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.ElectionAlgorithm;
import com.bsalazar.kekomo.general.FileSystem;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by bsalazar on 20/06/2017.
 */

public class CalendarDayDetailFragment extends Fragment {

  private ViewGroup rootView;
  private Context mContext;

  private LinearLayout container_layout, empty;
  private LinearLayout lunch_container, dinner_container;
  private TextView lunch, dinner;
  private TextView lunchDescription, dinnerDescription;

  private String date;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    rootView = (ViewGroup) inflater.inflate(R.layout.calendar_day_detail, container, false);
    mContext = getActivity().getApplicationContext();

    container_layout = rootView.findViewById(R.id.container);
    empty = rootView.findViewById(R.id.empty);

    lunch_container = rootView.findViewById(R.id.lunch_container);
    dinner_container = rootView.findViewById(R.id.dinner_container);

    lunch = rootView.findViewById(R.id.lunch);
    lunchDescription = rootView.findViewById(R.id.lunch_description);
    dinner = rootView.findViewById(R.id.dinner);
    dinnerDescription = rootView.findViewById(R.id.dinner_description);

    Button planLunch = rootView.findViewById(R.id.planing_lunch);
    Button planDinner = rootView.findViewById(R.id.planing_dinner);

    date = getArguments().getString("DATE", "");
    ArrayList<Event> events = (ArrayList<Event>) LocalDataSource.getInstance(mContext).getEventsByDate(date);

    if (events.size() == 0) {
      empty.setVisibility(View.VISIBLE);
      container_layout.setVisibility(View.GONE);
    } else {
      empty.setVisibility(View.GONE);
      container_layout.setVisibility(View.VISIBLE);
    }

    for (Event event : events) {
      Dish dish = LocalDataSource.getInstance(mContext).getDishByID(event.getDishID());
      switch (event.getType()) {
        case Constants.DISH_TYPE_LUNCH:
          lunch_container.setVisibility(View.VISIBLE);
          lunch.setText(dish.getName());
          lunchDescription.setText(dish.getDescription());
          planLunch.setVisibility(View.GONE);

          rootView.findViewById(R.id.delete_lunch_event).setOnClickListener(view -> {
            LocalDataSource.getInstance(mContext).deleteEvent(event);
            TransitionManager.beginDelayedTransition(container);
            lunch_container.setVisibility(View.GONE);
            planLunch.setVisibility(View.VISIBLE);
          });
          break;

        case Constants.DISH_TYPE_DINNER:
          dinner_container.setVisibility(View.VISIBLE);
          dinner.setText(dish.getName());
          dinnerDescription.setText(dish.getDescription());
          planDinner.setVisibility(View.GONE);

          rootView.findViewById(R.id.delete_dinner_event).setOnClickListener(view -> {
            LocalDataSource.getInstance(mContext).deleteEvent(event);
            TransitionManager.beginDelayedTransition(container);
            dinner_container.setVisibility(View.GONE);
            planDinner.setVisibility(View.VISIBLE);
          });
          break;
      }
    }

    planLunch.setOnClickListener(new PlanLunchClicked());
    planDinner.setOnClickListener(new PlanDinnerClicked());

    rootView.findViewById(R.id.planing_lunch_empty).setOnClickListener(new PlanLunchClicked());
    rootView.findViewById(R.id.planing_dinner_empty).setOnClickListener(new PlanDinnerClicked());

    return rootView;
  }

  class PlanLunchClicked implements View.OnClickListener {
    @Override
    public void onClick(View view) {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

        Intent i = new Intent(getActivity(), ElectionActivity.class);
        i.putExtra("date", date);
        i.putExtra("dishType", Constants.DISH_TYPE_LUNCH);

        String transitionName = getString(R.string.transitionName);
        view.setTransitionName(transitionName);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, transitionName);
        startActivityForResult(i, 20, transitionActivityOptions.toBundle());
      }
    }
  }

  class PlanDinnerClicked implements View.OnClickListener {
    @Override
    public void onClick(View view) {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

        Intent i = new Intent(getActivity(), ElectionActivity.class);
        i.putExtra("date", date);
        i.putExtra("dishType", Constants.DISH_TYPE_DINNER);

        String transitionName = getString(R.string.transitionName);
        view.setTransitionName(transitionName);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, transitionName);
        startActivityForResult(i, 20, transitionActivityOptions.toBundle());
      }
    }
  }
}