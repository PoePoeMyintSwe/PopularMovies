package com.poepoemyintswe.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.poepoemyintswe.popularmovies.Config;
import com.poepoemyintswe.popularmovies.R;
import com.poepoemyintswe.popularmovies.model.Result;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poepoe on 11/7/15.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
  ClickListener mItemClickListener;
  List<Result> results = new ArrayList<>();
  int height;

  public MovieAdapter(int height) {
    setHasStableIds(true);
    this.height = height;
  }

  public void setItems(List<Result> results) {
    this.results = results;
    notifyDataSetChanged();
  }

  public void setOnItemClickListener(final ClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    ViewGroup.LayoutParams params = holder.mImageView.getLayoutParams();
    params.height = height * 3;
    holder.mImageView.setLayoutParams(params);

    Glide.with(holder.mImageView.getContext())
        .load(Config.PHOTO_URL + results.get(position).getPosterPath())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .crossFade()
        .into(holder.mImageView);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemCount() {
    return results.size();
  }

  public interface ClickListener {
    void onItemClick(View view, int position);
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.iv_movie_image) ImageView mImageView;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      mImageView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (mItemClickListener != null) {
        mItemClickListener.onItemClick(view, getAdapterPosition());
      }
    }
  }
}
