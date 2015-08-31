package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.adapter.DetailAdapter;
import com.poepoemyintswe.popularmovies.model.Result;
import java.util.ArrayList;

import static com.poepoemyintswe.popularmovies.Config.POSITION;
import static com.poepoemyintswe.popularmovies.Config.RESULT;

/**
 * Created by poepoe on 1/9/15.
 */
public class MovieDetailFragmentContainer extends Fragment {

  @Bind(R.id.pager) ViewPager mPager;

  private ArrayList<Result> results;
  private int position;

  public static MovieDetailFragmentContainer newInstance(ArrayList<Result> results, int position) {
    MovieDetailFragmentContainer f = new MovieDetailFragmentContainer();
    Bundle args = new Bundle();
    args.putParcelableArrayList(RESULT, results);
    args.putInt(POSITION, position);
    f.setArguments(args);
    return f;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    results = getArguments().getParcelableArrayList(RESULT);
    position = getArguments().getInt(POSITION, 0);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ViewGroup rootView =
        (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail_container, container, false);
    ButterKnife.bind(this, rootView);

    mPager.setAdapter(new DetailAdapter(getActivity().getSupportFragmentManager(), results));
    mPager.setCurrentItem(position);

    Log.e("Result size", " "+ results.size());

    return rootView;
  }
}
