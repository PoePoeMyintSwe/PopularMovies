package com.poepoemyintswe.popularmovies.ui;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.adapter.MovieAdapter;
import com.poepoemyintswe.popularmovies.api.MyRestAdapter;
import com.poepoemyintswe.popularmovies.model.Movie;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

  @Bind(R.id.rv_movies) RecyclerView mRecyclerView;

  private MovieAdapter movieAdapter;
  public MainFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    ButterKnife.bind(this, view);
    initUI();
    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getMovies();
  }

  private void getMovies() {
    MyRestAdapter.getInstance()
        .getMovies()
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Movie>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(Movie movie) {
            movieAdapter.setItems(movie.getResults());
          }
        });
  }

  private void initUI() {
    GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setHasFixedSize(true);
    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_nano);
    mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    movieAdapter = new MovieAdapter(calculateWidth());
    mRecyclerView.setAdapter(movieAdapter);
  }

  private int calculateWidth() {
    int measuredWidth;
    WindowManager w = getActivity().getWindowManager();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      Point size = new Point();
      w.getDefaultDisplay().getSize(size);
      measuredWidth = size.x;
    } else {
      Display d = w.getDefaultDisplay();
      measuredWidth = d.getWidth();
    }
    measuredWidth = measuredWidth / 6;
    return measuredWidth;
  }
}
