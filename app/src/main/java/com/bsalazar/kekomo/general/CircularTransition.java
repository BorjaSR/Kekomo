package com.bsalazar.kekomo.general;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CircularTransition extends Transition {

  private static final String BOUNDS = "viewBounds";

  private static final String[] PROPS = {BOUNDS};

  @Override
  public void captureStartValues(TransitionValues transitionValues) {
    captureValues(transitionValues);
  }

  @Override
  public void captureEndValues(TransitionValues transitionValues) {
    captureValues(transitionValues);
  }

  private void captureValues(TransitionValues values) {
    View view = values.view;
    Rect bounds = new Rect();
    bounds.left = view.getLeft();
    bounds.right = view.getRight();
    bounds.top = view.getTop();
    bounds.bottom = view.getBottom();

    values.values.put(BOUNDS, bounds);
  }

  @Override
  public String[] getTransitionProperties() {
    return PROPS;
  }

  @Override
  public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
    if (startValues == null || endValues == null) {
      return null;
    }

    Rect startRect = (Rect) startValues.values.get(BOUNDS);
    final Rect endRect = (Rect) endValues.values.get(BOUNDS);

    final View view = endValues.view;

    Animator circularTransition;
    if (isReveal(startRect, endRect)) {
      circularTransition = createReveal(view, startRect, endRect);
      return new NoPauseAnimator(circularTransition);
    } else {
      layout(startRect, view);

      circularTransition = createConceal(view, startRect, endRect);
      circularTransition.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
              Rect bounds = endRect;
              bounds.left -= view.getLeft();
              bounds.top -= view.getTop();
              bounds.right -= view.getLeft();
              bounds.bottom -= view.getTop();
              outline.setOval(bounds);
              view.setClipToOutline(true);
            }
          });
        }
      });
      return new NoPauseAnimator(circularTransition);
    }
  }

  private void layout(Rect startRect, View view) {
    view.layout(startRect.left, startRect.top, startRect.right, startRect.bottom);
  }

  private Animator createReveal(View view, Rect from, Rect to) {

    int centerX = from.centerX();
    int centerY = from.centerY();
    float finalRadius = (float) Math.hypot(to.width(), to.height());

    return ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
        from.width()/2, finalRadius);
  }

  private Animator createConceal(View view, Rect from, Rect to) {

    int centerX = to.centerX();
    int centerY = to.centerY();
    float initialRadius = (float) Math.hypot(from.width(), from.height());

    return ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
        initialRadius, to.width()/2);
  }

  private boolean isReveal(Rect startRect, Rect endRect) {
    return startRect.width() < endRect.width();
  }
}