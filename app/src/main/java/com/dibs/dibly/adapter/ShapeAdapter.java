/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dibs.dibly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dibs.dibly.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.List;

public class ShapeAdapter extends RecyclerView.Adapter<ShapeAdapter.SimpleViewHolder> {

    private final Context mContext;
    private final TwoWayView mRecyclerView;
    private final List<String> mItems;
    private final int size;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imv;

        public SimpleViewHolder(View view) {
            super(view);
            imv = (ImageView) view.findViewById(R.id.imvShape);
        }
    }

    public ShapeAdapter(Context context, TwoWayView recyclerView, List<String> mItems) {
        mContext = context;
        this.mItems = new ArrayList<>();
        this.mItems.addAll(mItems);

        mRecyclerView = recyclerView;

        options = new DisplayImageOptions.Builder().cacheInMemory(true).showImageForEmptyUri(R.color.white).showImageOnLoading(R.color.white).cacheOnDisk(true).build();
        size = this.mItems.size();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.row_shape, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
//        holder.imv.setText(mItems.get(position).toString());
//        holder.imv.setImageResource(mItems.get(position));

        imageLoader.displayImage(mItems.get(position), holder.imv, options);

//        boolean isVertical = (mRecyclerView.getOrientation() == TwoWayLayoutManager.Orientation.VERTICAL);
//        final View itemView = holder.itemView;

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
