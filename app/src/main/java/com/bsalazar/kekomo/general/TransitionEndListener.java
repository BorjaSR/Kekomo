package com.bsalazar.kekomo.general;

import android.transition.Transition;

public abstract class TransitionEndListener implements Transition.TransitionListener {

  @Override
  public void onTransitionStart(Transition transition) {

  }

  @Override
  public abstract void onTransitionEnd(Transition transition);

  @Override
  public void onTransitionCancel(Transition transition) {

  }

  @Override
  public void onTransitionPause(Transition transition) {

  }

  @Override
  public void onTransitionResume(Transition transition) {

  }
}