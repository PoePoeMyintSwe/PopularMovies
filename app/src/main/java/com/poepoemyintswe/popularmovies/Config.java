package com.poepoemyintswe.popularmovies;

import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by poepoe on 11/7/15.
 */
public class Config {
  public static final String BASE_URL = "http://api.themoviedb.org/3";
  public static final String DISCOVER_MOVIE = "/discover/movie";
  public static final String PHOTO_URL = "http://image.tmdb.org/t/p/w185/";
  public static final String BACKDROP_URL = "http://image.tmdb.org/t/p/w342/";
  public static final String SORT_BY = "sort_by";
  public static final String PAGE = "page";
  public static final String API_KEY = "api_key";
  public static final String MOVIE= "movie";
  public static final String POSITION ="position";
  public static final String POPULARITY = "popularity.desc";
  public static final String RATING = "vote_average.desc";
  public static final String RATING_COUNT = "vote_count.desc";

  public static final String RESULT = "result";
  public static final String HEIGHT = "height";

  public static int calculateHeight(WindowManager w) {
    int measuredWidth;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      Point size = new Point();
      w.getDefaultDisplay().getSize(size);
      measuredWidth = size.x;
    } else {
      Display d = w.getDefaultDisplay();
      measuredWidth = d.getWidth();
    }
    return (int) (measuredWidth / 1.618);
  }
}
