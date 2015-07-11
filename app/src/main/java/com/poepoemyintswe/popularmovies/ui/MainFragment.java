package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.api.MyRestAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

  public MainFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    MyRestAdapter.getInstance().getMovies().subscribe();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }
}
