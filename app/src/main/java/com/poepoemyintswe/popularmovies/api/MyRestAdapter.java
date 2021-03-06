package com.poepoemyintswe.popularmovies.api;

import com.poepoemyintswe.popularmovies.BuildConfig;
import com.poepoemyintswe.popularmovies.model.Movie;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

import static com.poepoemyintswe.popularmovies.Config.API_KEY;
import static com.poepoemyintswe.popularmovies.Config.BASE_URL;
import static com.poepoemyintswe.popularmovies.Config.DISCOVER_MOVIE;
import static com.poepoemyintswe.popularmovies.Config.PAGE;
import static com.poepoemyintswe.popularmovies.Config.POPULARITY;
import static com.poepoemyintswe.popularmovies.Config.RATING;
import static com.poepoemyintswe.popularmovies.Config.RATING_COUNT;
import static com.poepoemyintswe.popularmovies.Config.SORT_BY;

/**
 * Created by poepoe on 11/7/15.
 */
public class MyRestAdapter {

  private MyService myService;
  private static MyRestAdapter myRestAdapter;

  public MyRestAdapter() {
    myService = restAdapter().create(MyService.class);
  }

  public static synchronized MyRestAdapter getInstance() {
    if (myRestAdapter == null) {
      myRestAdapter = new MyRestAdapter();
    }
    return myRestAdapter;
  }

  public RestAdapter restAdapter() {

    RestAdapter restAdapter;

    if (BuildConfig.DEBUG) {
      restAdapter = new retrofit.RestAdapter.Builder().setEndpoint(BASE_URL)
          .setLogLevel(RestAdapter.LogLevel.BASIC)
          .setClient(new OkClient(new OkHttpClient()))
          .build();
    } else {
      restAdapter = new retrofit.RestAdapter.Builder().setEndpoint(BASE_URL)
          .setClient(new OkClient(new OkHttpClient()))
          .build();
    }

    return restAdapter;
  }

  public Observable<Movie> getMovies() {
    return myService.getMovies(POPULARITY);
  }

  public Observable<Movie> getMoviesByRating() {
    return myService.getMovies(RATING, RATING_COUNT);
  }

  public Observable<Movie> getMoviesByPages(int page) {
    return myService.getMoviesByPages(POPULARITY, page);
  }

  public Observable<Movie> getMoviesByPagesNRating(int page) {
    return myService.getMoviesByPages(RATING, RATING_COUNT, page);
  }

  interface MyService {
    @GET(DISCOVER_MOVIE + "?" + API_KEY + "=" + BuildConfig.KEY) Observable<Movie> getMovies(
        @Query(SORT_BY) String sortBy1, @Query(SORT_BY) String sortBy2);

    @GET(DISCOVER_MOVIE + "?" + API_KEY + "=" + BuildConfig.KEY) Observable<Movie> getMovies(
        @Query(SORT_BY) String sortBy);

    @GET(DISCOVER_MOVIE + "?" + API_KEY + "=" + BuildConfig.KEY) Observable<Movie> getMoviesByPages(
        @Query(SORT_BY) String sortBy, @Query(PAGE) int page);

    @GET(DISCOVER_MOVIE + "?" + API_KEY + "=" + BuildConfig.KEY) Observable<Movie> getMoviesByPages(
        @Query(SORT_BY) String sortBy1, @Query(SORT_BY) String sortBy2, @Query(PAGE) int page);
  }
}
