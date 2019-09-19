package com.bsalazar.kekomo.ui_calendar;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.kekomo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bsalazar on 21/06/2017.
 */

public class CalendarActivity extends AppCompatActivity {


  private LinearLayout tolbar_controller;
  private Date selectedDate;
  private String selectedDateString;
  private SimpleDateFormat dateFormat, textDate;
  private CalendarView calendarView;
  private TextView dayText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    setToolbarBehaviour();

    dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    textDate = new SimpleDateFormat("EEEE, d MMM", Locale.getDefault());

    Fragment fragment = new CalendarDayDetailFragment();

    String dt = dateFormat.format(new Date());
    Bundle args = new Bundle();
    args.putString("DATE", dt);

    fragment.setArguments(args);

    last_fragment = fragment;
    getFragmentManager().beginTransaction()
        .replace(R.id.content, fragment)
        .addToBackStack(null)
        .commit();

    selectedDate = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate());
    selectedDateString = dateFormat.format(selectedDate);
    dayText = findViewById(R.id.dayText);
    calendarView = findViewById(R.id.calendar);
    calendarView.setOnDateChangeListener((calendarView1, year, month, day) -> {
      dayChanged(new Date(year - 1900, month, day));
    });
  }

  private void setToolbarBehaviour() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null)
      getSupportActionBar().setTitle(null);

    tolbar_controller = findViewById(R.id.tolbar_controller);
    AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
    appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
      TransitionManager.beginDelayedTransition(toolbar);
      tolbar_controller.setVisibility(Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0 ? View.VISIBLE : View.GONE);
      toolbar.setVisibility(Math.abs(verticalOffset) > 0 ? View.VISIBLE : View.GONE);
    });

    AnimatedVectorDrawableCompat animMonthLeftArrow = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_arrow_left);
    ImageView arrowLeft = findViewById(R.id.month_left);
    arrowLeft.setImageDrawable(animMonthLeftArrow);
    arrowLeft.setOnClickListener(view -> {
      if (animMonthLeftArrow != null) animMonthLeftArrow.start();
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date(calendarView.getDate()));
      cal.add(Calendar.DATE, -1);
      dayChanged(cal.getTime());
    });

    AnimatedVectorDrawableCompat animMonthRightArrow = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_arrow_right);
    ImageView arrowRight = findViewById(R.id.month_right);
    arrowRight.setImageDrawable(animMonthRightArrow);
    arrowRight.setOnClickListener(view -> {
      if (animMonthRightArrow != null) animMonthRightArrow.start();
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date(calendarView.getDate()));
      cal.add(Calendar.DATE, 1);
      dayChanged(cal.getTime());
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return true;
  }

  @Override
  public void onBackPressed() {
//        super.onBackPressed();
    finish();
  }

  private void dayChanged(Date newDate){
    boolean after = newDate.after(selectedDate);
    selectedDate = newDate;
    selectedDateString = dateFormat.format(selectedDate);
    changeFragment(after);
    calendarView.setDate(newDate.getTime(), true, true);
    dayText.setText(textDate.format(newDate));
  }

  private void setNameOfDay(){

  }

  private Fragment last_fragment;

  public void changeFragment(boolean after) {
    Fragment fragment = new CalendarDayDetailFragment();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Slide left_slide = new Slide();
      left_slide.setSlideEdge(Gravity.START);
      left_slide.setDuration(250);

      Slide right_slide = new Slide();
      right_slide.setSlideEdge(Gravity.END);
      right_slide.setDuration(250);

      if (after) {
        last_fragment.setExitTransition(left_slide);
        fragment.setEnterTransition(right_slide);
      } else {
        last_fragment.setExitTransition(right_slide);
        fragment.setEnterTransition(left_slide);
      }

      fragment.setAllowEnterTransitionOverlap(false);
      fragment.setAllowReturnTransitionOverlap(false);
    }

    Bundle args = new Bundle();
    args.putString("DATE", selectedDateString);
    fragment.setArguments(args);

    last_fragment = fragment;
    getFragmentManager().beginTransaction()
        .replace(R.id.content, fragment)
//                .replace(R.id.content, fragment)
        .addToBackStack(null)
        .commit();
  }
}
