package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.model.Result;

import static com.poepoemyintswe.popularmovies.Config.BACKDROP_URL;
import static com.poepoemyintswe.popularmovies.Config.PHOTO_URL;

/**
 * Created by poepoe on 11/7/15.
 */
public class MovieDetailFragment extends Fragment {

  @Bind(R.id.iv_backdrop) ImageView mBackDrop;
  @Bind(R.id.iv_movie_image) ImageView mPoster;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.tv_overview) TextView mTvOverview;
  @Bind(R.id.tv_release_date) TextView mTvReleaseDate;
  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.tv_language) TextView mTvLanguage;

  private AppCompatActivity mActivity;
  private static final String RESULT = "result";
  private Result result;
  private int height;

  public static MovieDetailFragment newInstance(Result result, int height) {
    MovieDetailFragment f = new MovieDetailFragment();
    Bundle args = new Bundle();
    args.putSerializable(RESULT, result);
    args.putInt("height", height);
    f.setArguments(args);
    return f;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = (AppCompatActivity) getActivity();
    if (getArguments() != null) {
      result = (Result) getArguments().getSerializable(RESULT);
      height = getArguments().getInt("height");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ViewGroup rootView =
        (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);
    ButterKnife.bind(this, rootView);

    //Actionbar
    mActivity.setSupportActionBar(toolbar);
    ActionBar mActionBar = mActivity.getSupportActionBar();
    mActionBar.setDisplayHomeAsUpEnabled(true);
    mActionBar.setTitle("");

    //Text
    mTvTitle.setText(result.getTitle());
    mTvOverview.setText(result.getOverview());
    mTvLanguage.setText(result.getOriginalLanguage());
    mTvReleaseDate.setText(result.getReleaseDate());

    //Photo
    ViewGroup.LayoutParams params = mBackDrop.getLayoutParams();
    params.height = height;
    mBackDrop.setLayoutParams(params);

    Glide.with(getActivity())
        .load(BACKDROP_URL + result.getBackdropPath())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.backdrop)
        .crossFade()
        .into(mBackDrop);
    Glide.with(getActivity())
        .load(PHOTO_URL + result.getPosterPath())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.placeholder)
        .crossFade()
        .into(mPoster);

    return rootView;
  }
}
