package com.poepoemyintswe.popularmovies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
  private static final String RESULT = "result";
  private Result result;

  public static MovieDetailFragment newInstace(Result result) {
    MovieDetailFragment f = new MovieDetailFragment();
    Bundle args = new Bundle();
    args.putSerializable(RESULT, result);
    f.setArguments(args);
    return f;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      result = (Result) getArguments().getSerializable(RESULT);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ViewGroup rootView =
        (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);
    ButterKnife.bind(this, rootView);
    Glide.with(getActivity())
        .load(BACKDROP_URL + result.getBackdropPath())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
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
