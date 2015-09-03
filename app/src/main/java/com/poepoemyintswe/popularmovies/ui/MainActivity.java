package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.poepoemyintswe.popularmovies.R;

public class MainActivity extends AppCompatActivity {

  boolean mTwoPane;
  private static final String DETAILFRAGMENT_TAG = "DFTAG";

  @Bind(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    if (findViewById(R.id.fragment_detail_container) != null) {
      mTwoPane = true;
      if (savedInstanceState == null) {
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_main_container, MainFragment.newInstance(mTwoPane))
            .commit();

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_detail_container, new MovieDetailFragment())
            .commit();
      }
    } else {
      mTwoPane = false;
      if (getSupportActionBar() != null) getSupportActionBar().setElevation(0f);
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_main_container, MainFragment.newInstance(mTwoPane))
          .commit();
    }
  }

  @Override protected void onResume() {
    super.onResume();
  }
}
