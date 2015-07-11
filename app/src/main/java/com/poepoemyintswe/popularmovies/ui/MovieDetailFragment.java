package com.poepoemyintswe.popularmovies.ui;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.model.Result;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.poepoemyintswe.popularmovies.Config.BACKDROP_URL;
import static com.poepoemyintswe.popularmovies.Config.PHOTO_URL;

/**
 * Created by poepoe on 11/7/15.
 */
public class MovieDetailFragment extends Fragment
    implements ObservableScrollView.ScrollViewListener {

  @Bind(R.id.iv_backdrop) ImageView mBackDrop;
  @Bind(R.id.iv_movie_image) ImageView mPoster;
  @Bind(R.id.fading_toolbar) Toolbar toolbar;
  @Bind(R.id.tv_overview) TextView mTvOverview;
  @Bind(R.id.tv_release_date) TextView mTvReleaseDate;
  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.tv_language) TextView mTvLanguage;
  @Bind(R.id.sv_content) ObservableScrollView mScrollView;
  @Bind(R.id.fl_backdrop_container) FrameLayout mBackdropFrame;
  @Bind(R.id.fl_thumbnail_container) FrameLayout mThumbnailFrame;

  private static final String RESULT = "result";
  private static final String HEIGHT = "height";

  private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
  private SpannableString mSpannableString;

  private Drawable mActionBarBackgroundDrawable;
  private int mActionBarTitleColor;

  private AppCompatActivity mActivity;
  private Result result;
  private int height;

  public static MovieDetailFragment newInstance(Result result, int height) {
    MovieDetailFragment f = new MovieDetailFragment();
    Bundle args = new Bundle();
    args.putSerializable(RESULT, result);
    args.putInt(HEIGHT, height);
    f.setArguments(args);
    return f;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = (AppCompatActivity) getActivity();
    if (getArguments() != null) {
      result = (Result) getArguments().getSerializable(RESULT);
      height = getArguments().getInt(HEIGHT);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ViewGroup rootView =
        (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);
    ButterKnife.bind(this, rootView);

    //Actionbar
    mActionBarBackgroundDrawable = toolbar.getBackground();

    mActionBarTitleColor = getResources().getColor(R.color.white);
    mSpannableString = new SpannableString(result.getOriginalTitle());
    mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(mActionBarTitleColor);


    mActivity.setSupportActionBar(toolbar);
    ActionBar mActionBar = mActivity.getSupportActionBar();
    mActionBar.setDisplayHomeAsUpEnabled(true);
    mActionBar.setTitle("");

    //Text
    mTvTitle.setText(result.getTitle());
    mTvOverview.setText(result.getOverview());
    mTvLanguage.setText(result.getOriginalLanguage());
    try {
      mTvReleaseDate.setText(formatDate());
    } catch (ParseException e) {
      e.printStackTrace();
    }

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

    mScrollView.setScrollViewListener(this);

    return rootView;
  }

  private String formatDate() throws ParseException {
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
    return outputFormat.format(inputFormat.parse(result.getReleaseDate()));
  }

  @Override public void onScrollChanged(ObservableScrollView scrollView, int y, int oldy) {
    int headerHeight = mBackdropFrame.getHeight() - toolbar.getHeight();
    float ratio = 0;
    if (oldy > 0 && headerHeight > 0) {
      ratio = (float) Math.min(Math.max(oldy, 0), headerHeight) / headerHeight;
    }
    mBackdropFrame.setTop((int) ratio < 0 ? 0 : (int) ratio);
    updateActionBarTransparency(ratio);
    setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
  }

  private void updateActionBarTransparency(float scrollRatio) {
    int newAlpha = (int) (scrollRatio * 255);
    mActionBarBackgroundDrawable.setAlpha(newAlpha);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      toolbar.setBackground(mActionBarBackgroundDrawable);
    } else {
      toolbar.setBackgroundDrawable(mActionBarBackgroundDrawable);
    }
  }

  private void setTitleAlpha(float alpha) {
    mAlphaForegroundColorSpan.setAlpha(alpha);
    mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    toolbar.setTitle(mSpannableString);
  }

  public static float clamp(float value, float min, float max) {
    return Math.max(min, Math.min(value, max));
  }

}
