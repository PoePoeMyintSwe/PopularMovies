package com.poepoemyintswe.popularmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.poepoemyintswe.popularmovies.model.Result;
import com.poepoemyintswe.popularmovies.ui.MovieDetailFragment;
import java.util.ArrayList;

/**
 * Created by poepoe on 11/7/15.
 */
public class DetailAdapter extends FragmentPagerAdapter {

  private ArrayList<Result> results;

  public DetailAdapter(FragmentManager fm, ArrayList<Result> results) {
    super(fm);
    this.results = results;

  }

  @Override public int getCount() {
    return results.size();
  }

  @Override public Fragment getItem(int position) {
    return MovieDetailFragment.newInstance(results.get(position));
  }
}
