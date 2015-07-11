package com.poepoemyintswe.popularmovies.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by poepoe on 12/7/15.
 */
public class ObservableScrollView extends ScrollView {
  private ScrollViewListener scrollViewListener = null;

  public ObservableScrollView(Context context) {
    super(context);
  }

  public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public ObservableScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setScrollViewListener(ScrollViewListener scrollViewListener) {
    this.scrollViewListener = scrollViewListener;
  }

  @Override protected void onScrollChanged(int x, int y, int oldx, int oldy) {
    super.onScrollChanged(x, y, oldx, oldy);
    if (scrollViewListener != null) {
      scrollViewListener.onScrollChanged(this, y, oldy);
    }
  }

  public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int y, int oldy);
  }
}
