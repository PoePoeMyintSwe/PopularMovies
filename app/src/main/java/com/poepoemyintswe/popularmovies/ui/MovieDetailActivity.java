package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.model.Result;
import java.util.ArrayList;

import static com.poepoemyintswe.popularmovies.Config.MOVIE;
import static com.poepoemyintswe.popularmovies.Config.POSITION;

public class MovieDetailActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);

    ArrayList<Result> results = getIntent().getParcelableArrayListExtra(MOVIE);
    int position = getIntent().getIntExtra(POSITION, 0);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_detail_container,
              MovieDetailFragmentContainer.newInstance(results, position))
          .commit();
    }
  }
}
