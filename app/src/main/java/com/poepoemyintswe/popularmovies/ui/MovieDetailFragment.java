package com.poepoemyintswe.popularmovies.ui;

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
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
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
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.tv_overview) TextView mTvOverview;
  @Bind(R.id.tv_release_date) TextView mTvReleaseDate;
  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.tv_language) TextView mTvLanguage;
  @Bind(R.id.sv_content) ObservableScrollView mScrollView;
  @Bind(R.id.fl_backdrop_container) FrameLayout mBackdropFrame;
  @Bind(R.id.fl_thumbnail_container) FrameLayout mThumbnailFrame;

  private boolean mFabIsShown;

  private static final String RESULT = "result";
  private static final String HEIGHT = "height";
  private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
  private SpannableString mSpannableString;
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

    ViewHelper.setScaleX(mThumbnailFrame, 1);
    ViewHelper.setScaleY(mThumbnailFrame, 1);

    mActivity.setSupportActionBar(toolbar);
    ActionBar mActionBar = mActivity.getSupportActionBar();
    mActionBar.setDisplayHomeAsUpEnabled(true);
    mActionBar.setTitle("");

    mActionBarTitleColor = getResources().getColor(R.color.white);

    mSpannableString = new SpannableString(result.getTitle());
    mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(mActionBarTitleColor);

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
    float py = y * .5f;
    toolbarFading(y);
    mBackdropFrame.setTop((int) py < 0 ? 0 : (int) py);
    int headerHeight = mBackdropFrame.getHeight() - toolbar.getHeight();
    float ratio = 0;
    if (oldy > 0 && headerHeight > 0) {
      ratio = (float) Math.min(Math.max(oldy, 0), headerHeight) / headerHeight;
    }
    setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
    if (y > ratio) {
      hideFab();
    } else {
      showFab();
    }
  }

  private void showFab() {
    if (!mFabIsShown) {
      ViewPropertyAnimator.animate(mTvTitle).cancel();
      ViewPropertyAnimator.animate(mTvTitle).scaleX(1).scaleY(1).setDuration(500).start();
      ViewPropertyAnimator.animate(mThumbnailFrame).cancel();
      ViewPropertyAnimator.animate(mThumbnailFrame).scaleX(1).scaleY(1).setDuration(300).start();

      mFabIsShown = true;
    }
  }

  private void hideFab() {
    if (mFabIsShown) {
      ViewPropertyAnimator.animate(mTvTitle).cancel();
      ViewPropertyAnimator.animate(mTvTitle).scaleX(0).scaleY(0).setDuration(500).start();
      ViewPropertyAnimator.animate(mThumbnailFrame).cancel();
      ViewPropertyAnimator.animate(mThumbnailFrame).scaleX(0).scaleY(0).setDuration(300).start();
      mFabIsShown = false;
    }
  }

  private void toolbarFading(int scrollY) {
    int baseColor = getResources().getColor(R.color.primary);
    float alpha = Math.min(1, (float) scrollY / height);
    toolbar.setBackgroundColor(getColorWithAlpha(alpha, baseColor));
    ViewHelper.setTranslationY(mBackDrop, scrollY / 2);
  }

  private int getColorWithAlpha(float alpha, int baseColor) {
    int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
    int rgb = 0x00ffffff & baseColor;
    return a + rgb;
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
