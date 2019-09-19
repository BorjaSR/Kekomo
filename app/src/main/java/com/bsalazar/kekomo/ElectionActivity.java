package com.bsalazar.kekomo;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.bsalazar.kekomo.general.CircularTransition;
import com.bsalazar.kekomo.general.ElectionAlgorithm;
import com.bsalazar.kekomo.general.TransitionEndListener;

import java.util.ArrayList;

public class ElectionActivity extends AppCompatActivity {

  private ElectionFragment electionFragment;

  private LinearLayout background;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_election);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      Transition transition = new CircularTransition();
      transition.setInterpolator(new LinearInterpolator());

      delay(0, new DelayFinishCallback() {
        @Override
        public void onDelayFinished() {
          ArrayList<Integer> dishList = new ElectionAlgorithm().calculateDishesList();
          if (dishList != null && dishList.size() > 0) {
            electionFragment = new ElectionFragment();
            Bundle args = new Bundle();
            args.putIntegerArrayList("dishes", dishList);
            electionFragment.setArguments(args);

            getFragmentManager().beginTransaction()
                .replace(R.id.frame_container, electionFragment)
                .addToBackStack(null)
                .commit();
          } else
            Snackbar.make(background, "Todavia no tienes platos guardados", Snackbar.LENGTH_SHORT).show();
        }
      });

      getWindow().setSharedElementEnterTransition(transition);
    }

    background = findViewById(R.id.background);
    animateBackground();
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(String name, Context context, AttributeSet attrs) {
    return super.onCreateView(name, context, attrs);
  }

  boolean dishSelected = false;
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (getSupportFragmentManager().getFragments().size() == 0)
      delay(200, new DelayFinishCallback() {
        @Override
        public void onDelayFinished() {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              reverseBackground();
              setResult(dishSelected ? RESULT_OK : RESULT_CANCELED);
              ElectionActivity.super.onBackPressed();
            }
          });
        }
      });
  }

  interface DelayFinishCallback {
    void onDelayFinished();
  }

  private void delay(final int delay, final DelayFinishCallback callback) {
    Thread timer = new Thread() {
      public void run() {
        try {
          sleep(delay);
          callback.onDelayFinished();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }
    };
    timer.start();
  }

  public void animateBackground() {
    int colorFrom = getResources().getColor(R.color.colorAccent);
    int colorTo = getResources().getColor(R.color.white);
    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
    colorAnimation.setDuration(250);
    colorAnimation.setStartDelay(250);
    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

      @Override
      public void onAnimationUpdate(ValueAnimator animator) {
        background.setBackgroundColor((int) animator.getAnimatedValue());
      }

    });
    colorAnimation.start();
  }

  public void reverseBackground() {
    int colorFrom = getResources().getColor(R.color.white);
    int colorTo = getResources().getColor(R.color.colorAccent);
    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
    colorAnimation.setDuration(250);
    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

      @Override
      public void onAnimationUpdate(ValueAnimator animator) {
        background.setBackgroundColor((int) animator.getAnimatedValue());
      }

    });
    colorAnimation.start();
  }
}
