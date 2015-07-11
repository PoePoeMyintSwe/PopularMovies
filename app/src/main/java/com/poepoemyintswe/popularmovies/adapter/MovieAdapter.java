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
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  ClickListener mItemClickListener;

  private boolean isFooterEnabled = false;
  private List<Result> results = new ArrayList<>();
  private int height;

  public static final int ITEM = 1;
  public static final int FOOTER = 2;

  public MovieAdapter(int height) {
    setHasStableIds(true);
    this.height = height;
  }

  public void setItems(List<Result> results) {
    this.results = results;
    notifyDataSetChanged();
  }

  public void addMoreItems(List<Result> movies) {
    results.addAll(movies);
    notifyItemRangeInserted(results.size() - movies.size(), movies.size());
  }

  public void setOnItemClickListener(final ClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);
      return new ViewHolder(view);
    } else { //Footer
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.footer_progressbar, parent, false);
      return new Footer(view);
    }

  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ViewHolder) {
      ViewGroup.LayoutParams params = ((ViewHolder) holder).mImageView.getLayoutParams();
      params.height = height * 3;
      ((ViewHolder) holder).mImageView.setLayoutParams(params);

      Glide.with(holder.itemView.getContext())
          .load(Config.PHOTO_URL + results.get(position).getPosterPath())
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .crossFade()
          .into(((ViewHolder) holder).mImageView);
    }

  }

  @Override public int getItemViewType(int position) {
    return (isFooterEnabled && position >= results.size()) ? FOOTER : ITEM;
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemCount() {
    return (isFooterEnabled) ? results.size() + 1 : results.size();
  }

  public void setFooterEnabled(boolean footerEnabled) {
    this.isFooterEnabled = footerEnabled;
    if (footerEnabled) {
      notifyItemInserted(results.size());
    } else {
      notifyItemRemoved(results.size());
    }
  }

  public interface ClickListener {
    void onItemClick(View view, int position);
  }

  public static class Footer extends RecyclerView.ViewHolder {
    public Footer(View itemView) {
      super(itemView);
    }
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
