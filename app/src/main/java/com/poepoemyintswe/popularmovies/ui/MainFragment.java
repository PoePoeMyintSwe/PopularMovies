package com.poepoemyintswe.popularmovies.ui;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.pnikosis.materialishprogress.ProgressWheel;
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
  @Bind(R.id.progress_bar) ProgressWheel progressWheel;

  private int pageNum = 1;
  private boolean canLoadMore = true, loadInProgress = false;
  private GridLayoutManager mLayoutManager;

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

  private void loadMoreMovies() {
    MyRestAdapter.getInstance()
        .getMoviesByPages(pageNum)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Movie>() {
          @Override public void onCompleted() {
            loadInProgress = false;
            movieAdapter.setFooterEnabled(false);
          }

          @Override public void onError(Throwable e) {
            loadInProgress = false;
            canLoadMore = false;
            movieAdapter.setFooterEnabled(false);
          }

          @Override public void onNext(Movie movie) {
            if (movie.getResults().size() > 0) {
              movieAdapter.addMoreItems(movie.getResults());
            } else {
              canLoadMore = false;
            }
          }
        });
  }

  private void getMovies() {
    MyRestAdapter.getInstance().getMovies()
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Movie>() {
          @Override public void onCompleted() {
            progressWheel.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
          }

          @Override public void onError(Throwable e) {
            Log.e("Error", e.getLocalizedMessage());
          }

          @Override public void onNext(Movie movie) {
            movieAdapter.setItems(movie.getResults());
          }
        });
  }

  private void initUI() {
    mLayoutManager = new GridLayoutManager(getActivity(), 3);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setHasFixedSize(true);
    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_nano);
    mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    movieAdapter = new MovieAdapter(calculateWidth());
    mRecyclerView.setAdapter(movieAdapter);
    mRecyclerView.setVisibility(View.GONE);
    mRecyclerView.addOnScrollListener(new ScrollListener());
     

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

  public void loadNextPage() {
    pageNum++;
    loadMoreMovies();
    movieAdapter.setFooterEnabled(true);
    loadInProgress = true;
  }

  private class ScrollListener extends RecyclerView.OnScrollListener {
    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      if (canLoadMore && !loadInProgress) {
        int numVisibleItems = recyclerView.getChildCount();
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

        if (firstVisibleItem + numVisibleItems
            >= movieAdapter.getItemCount()) { //Reached the end of the list
          loadNextPage();
        }
      }
    }
  }
}
