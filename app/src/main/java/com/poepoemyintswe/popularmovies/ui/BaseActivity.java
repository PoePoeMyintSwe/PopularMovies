package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.poepoemyintswe.popularmovies.R;

/**
 * Created by poepoe on 1/9/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutResource());
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
  }

  protected abstract int getLayoutResource();
}
