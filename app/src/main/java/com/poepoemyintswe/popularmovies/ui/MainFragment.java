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
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.adapter.MovieAdapter;
import com.poepoemyintswe.popularmovies.api.MyRestAdapter;
import com.poepoemyintswe.popularmovies.model.Movie;
import java.io.Serializable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.poepoemyintswe.popularmovies.Config.MOVIE;
import static com.poepoemyintswe.popularmovies.Config.POSITION;
import static com.poepoemyintswe.popularmovies.Config.SORT_ORDER;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

  @Bind(R.id.rv_movies) RecyclerView mRecyclerView;
  @Bind(R.id.progress_bar) ProgressWheel mProgressWheel;

  final CharSequence[] sortBy = { " Popularity ", " Highest Rating " };
  AlertDialog mDialog;
  private String sort;
  private int check = 0;
  private int pageNum = 1;
  private boolean canLoadMore = true, loadInProgress = false;
  private GridLayoutManager mLayoutManager;

  private MovieAdapter movieAdapter;
  public MainFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onStart() {
    super.onStart();
    getMovies();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    ButterKnife.bind(this, view);
    initUI();
    return view;
  }

  private void loadMoreMovies() {
    MyRestAdapter.getInstance().getMoviesByPages(sort, pageNum)
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
    MyRestAdapter.getInstance().getMovies(sort)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Movie>() {
          @Override public void onCompleted() {
            mProgressWheel.setVisibility(View.GONE);
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
    sort = SORT_ORDER[0];
    mLayoutManager = new GridLayoutManager(getActivity(), 3);
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
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MOVIE, (Serializable) movieAdapter.getResults());
        intent.putExtra(POSITION, position);
        startActivity(intent);
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

  private void showSortDialog() {
    for (int i = 0; i < SORT_ORDER.length; i++) {
      if (sort.equals(SORT_ORDER)) check = i;
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Sort movies by");
    builder.setSingleChoiceItems(sortBy, check, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int item) {
        if (!sort.equals(SORT_ORDER[item])) {

          check = item;
          sort = SORT_ORDER[item];
          getMovies();
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

    new android.app.AlertDialog.Builder(getActivity()).setTitle(R.string.action_license)
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
