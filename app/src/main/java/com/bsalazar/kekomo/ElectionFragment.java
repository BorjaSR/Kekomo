package com.bsalazar.kekomo;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.Locale;

/**
 * Created by bsalazar on 20/06/2017.
 */

public class ElectionFragment extends Fragment implements View.OnClickListener {

  private ViewGroup rootView;
  private Context mContext;
  private ArrayList<Integer> dishes;
  private Integer actual_dish = 0;

  private ImageView dish_image, no, yes;
  private TextView dihs_name, dish_description;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Transition trans = TransitionInflater.from(getActivity().getApplicationContext()).inflateTransition(R.transition.transition_sample);

      setEnterTransition(trans);
      setReturnTransition(new Fade());


      setAllowEnterTransitionOverlap(false);
      setAllowReturnTransitionOverlap(false);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    rootView = (ViewGroup) inflater.inflate(R.layout.election_fragment, container, false);
    mContext = getActivity().getApplicationContext();

    dishes = getArguments().getIntegerArrayList("dishes");

    dish_image = rootView.findViewById(R.id.dish_image);
    dihs_name = rootView.findViewById(R.id.dihs_name);
    dish_description = rootView.findViewById(R.id.dish_description);
    no = rootView.findViewById(R.id.no);
    yes = rootView.findViewById(R.id.yes);

    no.setOnClickListener(this);
    yes.setOnClickListener(this);

    configureLayout();

    return rootView;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.no:

        Animation connectingAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shake);
        no.startAnimation(connectingAnimation);

        actual_dish++;
        if (actual_dish == dishes.size()) actual_dish = 0;
        configureLayout();
        break;

      case R.id.yes:
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Dish dish = LocalDataSource.getInstance(mContext).getDishByID(dishes.get(actual_dish));
        Event event = new Event();
        event.setDishID(dish.getId());
        event.setDate(dateFormat.format(new Date()));

        if (new Date().getHours() <= Constants.LUNCH_TIME)
          event.setType(Constants.DISH_TYPE_LUNCH);
        else
          event.setType(Constants.DISH_TYPE_DINNER);

        LocalDataSource.getInstance(mContext).saveEvent(event);
        ((ElectionActivity) getActivity()).dishSelected = true;
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
    dish_description.setText(dish.getDescription());
  }

}
