package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import com.poepoemyintswe.popularmovies.R;

public class MainActivity extends BaseActivity {

  boolean mTwoPane;
  private static final String DETAILFRAGMENT_TAG = "DFTAG";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (findViewById(R.id.fragment_detail_container) != null) {
      mTwoPane = true;
      if (savedInstanceState == null) {
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_detail_container, new MovieDetailFragment())
            .commit();
      }
    } else {
      mTwoPane = false;
      getSupportActionBar().setElevation(0f);
    }
  }

  @Override protected int getLayoutResource() {
    return R.layout.activity_main;
  }
}
