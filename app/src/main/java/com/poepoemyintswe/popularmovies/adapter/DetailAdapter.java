package com.poepoemyintswe.popularmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.poepoemyintswe.popularmovies.model.Result;
import com.poepoemyintswe.popularmovies.ui.MovieDetailFragment;
import java.util.List;

/**
 * Created by poepoe on 11/7/15.
 */
public class DetailAdapter extends FragmentPagerAdapter {

  private List<Result> results;

  public DetailAdapter(FragmentManager fm, List<Result> results) {
    super(fm);
    this.results = results;
  }

  @Override public int getCount() {
    return results.size();
  }

  @Override public Fragment getItem(int position) {
    return MovieDetailFragment.newInstace(results.get(position));
  }
}
