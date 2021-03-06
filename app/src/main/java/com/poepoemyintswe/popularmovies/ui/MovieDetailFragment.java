package com.poepoemyintswe.popularmovies.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
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
import com.poepoemyintswe.popularmovies.Config;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.model.Result;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.poepoemyintswe.popularmovies.Config.BACKDROP_URL;
import static com.poepoemyintswe.popularmovies.Config.PHOTO_URL;
import static com.poepoemyintswe.popularmovies.Config.RESULT;

/**
 * Created by poepoe on 11/7/15.
 */
public class MovieDetailFragment extends Fragment
    implements ObservableScrollView.ScrollViewListener {

  @Bind(R.id.iv_backdrop) ImageView mBackDrop;
  @Bind(R.id.iv_movie_image) ImageView mPoster;
  @Bind(R.id.detail_toolbar) Toolbar toolbar;
  @Bind(R.id.tv_overview) TextView mTvOverview;
  @Bind(R.id.tv_release_date) TextView mTvReleaseDate;
  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.tv_language) TextView mTvLanguage;
  @Bind(R.id.sv_content) ObservableScrollView mScrollView;
  @Bind(R.id.fl_backdrop_container) FrameLayout mBackdropFrame;
  @Bind(R.id.fl_thumbnail_container) FrameLayout mThumbnailFrame;
  @Bind(R.id.rb_rating) AppCompatRatingBar mRatingBar;
  @Bind(R.id.tv_vote) TextView mTvVote;

  private boolean mFabIsShown;
  private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
  private SpannableString mSpannableString;
  private int mActionBarTitleColor;

  private AppCompatActivity mActivity;
  private Result result;
  private int height;

  public static MovieDetailFragment newInstance(Result result) {
    MovieDetailFragment f = new MovieDetailFragment();
    Bundle args = new Bundle();
    args.putParcelable(RESULT, result);
    f.setArguments(args);
    return f;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = (AppCompatActivity) getActivity();
    if (getArguments() != null) {
      result = getArguments().getParcelable(RESULT);
    }
    height = Config.calculateHeight(getActivity().getWindowManager());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ViewGroup rootView =
        (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);
    ButterKnife.bind(this, rootView);

    ViewHelper.setScaleX(mThumbnailFrame, 1);
    ViewHelper.setScaleY(mThumbnailFrame, 1);

    mActivity.setSupportActionBar(toolbar);
    if (mActivity.getSupportActionBar() != null) {
      ActionBar mActionBar = mActivity.getSupportActionBar();
      mActionBar.setDisplayHomeAsUpEnabled(true);
      mActionBar.setTitle("");
    }
    mActionBarTitleColor = getResources().getColor(R.color.white);

    if (result != null) {
      mSpannableString = new SpannableString(result.getTitle());
      mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(mActionBarTitleColor);

      //Text
      mTvTitle.setText(result.getTitle());
      mTvOverview.setText(result.getOverview());
      mTvLanguage.setText(result.getOriginalLanguage());
      mTvVote.setText("Total " + result.getVoteCount());
      try {
        mTvReleaseDate.setText(formatDate());
      } catch (ParseException e) {
        e.printStackTrace();
      }

      //rating
      customRatingBar();
      mRatingBar.setRating((float) result.getVoteAverage());

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
    }

    return rootView;
  }

  private String formatDate() throws ParseException {
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
    return outputFormat.format(inputFormat.parse(result.getReleaseDate()));
  }

  @Override public void onScrollChanged(ObservableScrollView scrollView, int y, int oldy) {
    float py = y * .5f;
    toolbarFading(scrollView.getScrollY());
    mBackdropFrame.setTop((int) py < 0 ? 0 : (int) py);
    mTvTitle.setTop((int) py < 0 ? 0 : (int) py);
    int headerHeight = mBackdropFrame.getHeight() - toolbar.getHeight();
    float ratio = 0;
    if (oldy > 0 && headerHeight > 0) {
      ratio = (float) Math.min(Math.max(oldy, 0), headerHeight) / headerHeight;
    }
    setTitleAlpha(clamp(5.0F *py  - 4.0F, 0.0F, 1.0F));
    if (y > ratio) {
      hideFab();
    } else {
      showFab();
    }
  }

  private void showFab() {
    if (!mFabIsShown) {
      ViewPropertyAnimator.animate(mThumbnailFrame).cancel();
      ViewPropertyAnimator.animate(mThumbnailFrame).scaleX(1).scaleY(1).setDuration(300).start();

      mFabIsShown = true;
    }
  }

  private void hideFab() {
    if (mFabIsShown) {
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

  private void customRatingBar() {
    LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
    stars.getDrawable(2).setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.SRC_ATOP);
    stars.getDrawable(1).setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
    stars.getDrawable(0).setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
    mRatingBar.setProgressDrawable(stars);
  }

}
