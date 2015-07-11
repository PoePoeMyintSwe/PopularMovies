package com.poepoemyintswe.popularmovies;

import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

import static com.poepoemyintswe.popularmovies.Config.API_KEY;
import static com.poepoemyintswe.popularmovies.Config.BASE_URL;
import static com.poepoemyintswe.popularmovies.Config.DISCOVER_MOVIE;

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
          .setLogLevel(RestAdapter.LogLevel.FULL)
          .setClient(new OkClient(new OkHttpClient()))
          .build();
    } else {
      restAdapter = new retrofit.RestAdapter.Builder().setEndpoint(BASE_URL)
          .setClient(new OkClient(new OkHttpClient()))
          .build();
    }

    return restAdapter;
  }

  public Observable<JsonObject> getMovies() {
    return myService.getMovies("popularity.desc");
  }

  interface MyService {
    @GET(DISCOVER_MOVIE + "?" + API_KEY + "=" + BuildConfig.KEY) Observable<JsonObject> getMovies(
        @Query("sort_by") String sortBy);
  }
}
