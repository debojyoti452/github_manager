/*
 * *******************************************************************************************
 *  * Created By Debojyoti Singha
 *  * Copyright (c) 2022.
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 * ******************************************************************************************
 */

package com.swing.githubmanager.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.swing.githubmanager.core.abstraction.BaseAdapter
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.databinding.LayoutLoadingViewBinding
import com.swing.githubmanager.databinding.LayoutTrendingItemBinding
import com.swing.githubmanager.presentation.adapter.viewholder.LoadingViewHolder
import com.swing.githubmanager.presentation.adapter.viewholder.TrendingViewHolder
import com.swing.githubmanager.presentation.widgets.MultiColorProgressView

class TrendingAdapter(
    private val onItemClicked: (TrendingDataModel, Triple<AppCompatImageView, AppCompatTextView, MultiColorProgressView>) -> Unit
) : BaseAdapter<TrendingViewHolder, LoadingViewHolder, TrendingDataModel>() {

    override fun getViewHolder(viewGroup: ViewGroup, viewType: Int): TrendingViewHolder =
        TrendingViewHolder(
            LayoutTrendingItemBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )

    override fun getLoadingViewHolder(viewGroup: ViewGroup, viewType: Int): LoadingViewHolder =
        LoadingViewHolder(
            LayoutLoadingViewBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )

    override fun bind(holder: RecyclerView.ViewHolder, data: TrendingDataModel, position: Int) {
        if (holder is TrendingViewHolder) {
            holder.bindView(data, position)
            holder.itemView.setOnClickListener {
                onItemClicked(
                    data,
                    Triple(
                        holder.binding.itemTrendingIV,
                        holder.binding.itemTrendingDescTV,
                        holder.binding.itemLanguageGraphView
                    )
                )
            }
        } else if (holder is LoadingViewHolder) {
            holder.bindView(data, position)
        }
    }
}
