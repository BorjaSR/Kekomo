package com.bsalazar.kekomo.ui_calendar;

import  android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.bsalazar.kekomo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bsalazar on 21/06/2017.
 */

public class CalendarActivity extends AppCompatActivity {


    private Date selectedDate;
    private String selectedDateString;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Date new_date = new Date(year - 1900, month, day);
                boolean after = new_date.after(selectedDate);
                selectedDate = new_date;
                selectedDateString = dateFormat.format(selectedDate);
                changeFragment(after);

            }
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
