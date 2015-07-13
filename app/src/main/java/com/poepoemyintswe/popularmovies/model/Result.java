package com.poepoemyintswe.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by poepoe on 11/7/15.
 */

public class Result implements Parcelable {

  @SerializedName("backdrop_path") @Expose private String backdropPath;
  @SerializedName("original_language") @Expose private String originalLanguage;
  @SerializedName("original_title") @Expose private String originalTitle;
  @Expose private String overview;
  @SerializedName("release_date") @Expose private String releaseDate;
  @SerializedName("poster_path") @Expose private String posterPath;
  @Expose private String title;
  @SerializedName("vote_average") @Expose private double voteAverage;
  @SerializedName("vote_count") @Expose private int voteCount;

  private Result(Parcel in) {
    backdropPath = in.readString();
    originalLanguage = in.readString();
    originalTitle = in.readString();
    overview = in.readString();
    releaseDate = in.readString();
    posterPath = in.readString();
    title = in.readString();
    voteAverage = in.readDouble();
    voteCount = in.readInt();
  }

  public static final Creator<Result> CREATOR = new Creator<Result>() {
    @Override public Result createFromParcel(Parcel in) {
      return new Result(in);
    }

    @Override public Result[] newArray(int size) {
      return new Result[size];
    }
  };

  /**
   * @return The backdropPath
   */
  public String getBackdropPath() {
    return backdropPath;
  }

  /**
   * @param backdropPath The backdrop_path
   */
  public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }

  /**
   * @return The originalLanguage
   */
  public String getOriginalLanguage() {
    return originalLanguage;
  }

  /**
   * @param originalLanguage The original_language
   */
  public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
  }

  /**
   * @return The originalTitle
   */
  public String getOriginalTitle() {
    return originalTitle;
  }

  /**
   * @param originalTitle The original_title
   */
  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  /**
   * @return The overview
   */
  public String getOverview() {
    return overview;
  }

  /**
   * @param overview The overview
   */
  public void setOverview(String overview) {
    this.overview = overview;
  }

  /**
   * @return The releaseDate
   */
  public String getReleaseDate() {
    return releaseDate;
  }

  /**
   * @param releaseDate The release_date
   */
  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  /**
   * @return The posterPath
   */
  public String getPosterPath() {
    return posterPath;
  }

  /**
   * @param posterPath The poster_path
   */
  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  /**
   * @return The title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title The title
   */
  public void setTitle(String title) {
    this.title = title;
  }
  /**
   * @return The voteAverage
   */
  public double getVoteAverage() {
    return voteAverage;
  }

  /**
   * @param voteAverage The vote_average
   */
  public void setVoteAverage(double voteAverage) {
    this.voteAverage = voteAverage;
  }

  /**
   * @return The voteCount
   */
  public int getVoteCount() {
    return voteCount;
  }

  /**
   * @param voteCount The vote_count
   */
  public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
  }

  @Override public String toString() {
    return backdropPath
        + ":"
        + originalLanguage
        + ":"
        + originalTitle
        + ":"
        + overview
        + ":"
        + releaseDate
        + ":"
        + posterPath
        + ":"
        + title
        + ":"
        + voteAverage
        + ":"
        + voteCount;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(backdropPath);
    dest.writeString(originalLanguage);
    dest.writeString(originalTitle);
    dest.writeString(overview);
    dest.writeString(releaseDate);
    dest.writeString(posterPath);
    dest.writeString(title);
    dest.writeDouble(voteAverage);
    dest.writeInt(voteCount);
  }
}