package com.poepoemyintswe.popularmovies.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.adapter.MovieAdapter;
import com.poepoemyintswe.popularmovies.api.MyRestAdapter;
import com.poepoemyintswe.popularmovies.model.Movie;
import com.poepoemyintswe.popularmovies.model.Result;
import java.util.ArrayList;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.poepoemyintswe.popularmovies.Config.MOVIE;
import static com.poepoemyintswe.popularmovies.Config.POSITION;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

  @Bind(R.id.rv_movies) RecyclerView mRecyclerView;
  @Bind(R.id.progress_bar) ProgressWheel mProgressWheel;

  final CharSequence[] sortBy = { " Popularity ", " Highest Rating " };
  AlertDialog mDialog;

  ArrayList<Result> resultList;

  private int check = 0;
  private int pageNum = 1;
  private boolean canLoadMore = true, loadInProgress = false;
  private GridLayoutManager mLayoutManager;

  private MovieAdapter movieAdapter;
  private boolean mTwoPane;

  public static MainFragment newInstance(boolean twoPane) {
    MainFragment fragment = new MainFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean("TwoPane", twoPane);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    mTwoPane = getArguments().getBoolean("TwoPane");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    ButterKnife.bind(this, view);
    initUI();

    if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE)) {
      getMovies();
    } else {
      resultList = savedInstanceState.getParcelableArrayList(MOVIE);
      movieAdapter.setItems(resultList);
      mProgressWheel.setVisibility(View.GONE);
      mRecyclerView.setVisibility(View.VISIBLE);
    }

    return view;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(MOVIE, movieAdapter.getResults());
  }

  private void loadMoreMovies() {
    MyRestAdapter.getInstance().getMoviesByPages(pageNum)
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

  private void loadMoreMoviesByRating() {
    MyRestAdapter.getInstance()
        .getMoviesByPagesNRating(pageNum)
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
            mProgressWheel.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
          }

          @Override public void onError(Throwable e) {
            mProgressWheel.setVisibility(View.GONE);
            Toast.makeText(getActivity(),
                "Sorry! Something went wrong! Caused by " + e.getLocalizedMessage(),
                Toast.LENGTH_SHORT).show();
          }

          @Override public void onNext(Movie movie) {
            movieAdapter.setItems(movie.getResults());
          }
        });
  }

  private void getMoviesByRating() {
    MyRestAdapter.getInstance()
        .getMoviesByRating()
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Movie>() {
          @Override public void onCompleted() {
            mProgressWheel.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
          }

          @Override public void onError(Throwable e) {
            mProgressWheel.setVisibility(View.GONE);
            Toast.makeText(getActivity(),
                "Sorry! Something went wrong! Caused by " + e.getLocalizedMessage(),
                Toast.LENGTH_SHORT).show();
          }

          @Override public void onNext(Movie movie) {
            movieAdapter.setItems(movie.getResults());
          }
        });
  }

  private void initUI() {
    if (mTwoPane) {
      mLayoutManager = new GridLayoutManager(getActivity(), 2);
    } else {
      mLayoutManager = new GridLayoutManager(getActivity(), 3);
    }

    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setHasFixedSize(true);
    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_nano);
    mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    movieAdapter = new MovieAdapter(calculateWidth());
    mRecyclerView.setAdapter(movieAdapter);
    mRecyclerView.setVisibility(View.GONE);
    mRecyclerView.addOnScrollListener(new ScrollListener());

    movieAdapter.setOnItemClickListener(new MovieAdapter.ClickListener() {
      @Override public void onItemClick(View view, int position) {
        if (mTwoPane) {

        } else {
          Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
          intent.putParcelableArrayListExtra(MOVIE, movieAdapter.getResults());
          intent.putExtra(POSITION, position);
          startActivity(intent);
        }

      }
    });
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
    if (check == 0) {
      loadMoreMovies();
    } else {
      loadMoreMoviesByRating();
    }
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

  private void showSortDialog() {
    AlertDialog.Builder builder =
        new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
    builder.setTitle("Sort movies by");
    builder.setSingleChoiceItems(sortBy, check, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int item) {
        if (check != item) {
          check = item;
          pageNum = 1;
          mRecyclerView.scrollToPosition(0);
          if (item == 0) {
            getMovies();
          } else {
            getMoviesByRating();
          }
        }
        mDialog.dismiss();
      }
    });
    mDialog = builder.create();
    mDialog.show();
  }

  private void showLicense() {
    WebView webView = new WebView(getActivity());
    webView.loadUrl("file:///android_asset/licenses.html");

    new android.app.AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle).setTitle(
        R.string.action_license)
        .setView(webView)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            dialog.dismiss();
          }
        })
        .create()
        .show();
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_main, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_sort:
        showSortDialog();
        return true;
      case R.id.license:
        showLicense();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
