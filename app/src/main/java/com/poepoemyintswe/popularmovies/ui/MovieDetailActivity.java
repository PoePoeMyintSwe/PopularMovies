package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.adapter.DetailAdapter;
import com.poepoemyintswe.popularmovies.model.Result;
import java.util.List;

import static com.poepoemyintswe.popularmovies.Config.MOVIE;
import static com.poepoemyintswe.popularmovies.Config.POSITION;

public class MovieDetailActivity extends FragmentActivity {

  @Bind(R.id.pager) ViewPager mPager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    ButterKnife.bind(this);
    getMovieList();
  }

  public void getMovieList() {
    List<Result> results = (List<Result>) getIntent().getSerializableExtra(MOVIE);
    int position = getIntent().getIntExtra(POSITION, 0);
    mPager.setAdapter(new DetailAdapter(getSupportFragmentManager(), results));
    mPager.setCurrentItem(position);
  }
}
