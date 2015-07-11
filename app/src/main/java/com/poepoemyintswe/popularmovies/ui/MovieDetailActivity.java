package com.poepoemyintswe.popularmovies.ui;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.adapter.DetailAdapter;
import com.poepoemyintswe.popularmovies.model.Result;
import java.util.List;

import static com.poepoemyintswe.popularmovies.Config.MOVIE;
import static com.poepoemyintswe.popularmovies.Config.POSITION;

public class MovieDetailActivity extends AppCompatActivity {

  @Bind(R.id.pager) ViewPager mPager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    ButterKnife.bind(this);
    getMovieList();
  }

  public void getMovieList() {
    Log.e(MovieDetailActivity.class.getSimpleName(), "" + calculateHeight());
    List<Result> results = (List<Result>) getIntent().getSerializableExtra(MOVIE);
    int position = getIntent().getIntExtra(POSITION, 0);
    mPager.setAdapter(new DetailAdapter(getSupportFragmentManager(), results, calculateHeight()));
    mPager.setCurrentItem(position);
  }

  private int calculateHeight() {
    int measuredWidth;
    WindowManager w = getWindowManager();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      Point size = new Point();
      w.getDefaultDisplay().getSize(size);
      measuredWidth = size.x;
    } else {
      Display d = w.getDefaultDisplay();
      measuredWidth = d.getWidth();
    }
    return (int) (measuredWidth / 1.618);
  }
}
