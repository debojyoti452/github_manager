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

package com.swing.githubmanager.core.abstraction

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swing.githubmanager.core.log.Log

enum class ViewType {
    VIEW_ITEM, VIEW_PROGRESS
}

abstract class BaseAdapter<VH : RecyclerView.ViewHolder, LH : RecyclerView.ViewHolder, Data> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataList: MutableList<Data> = mutableListOf()

    override fun getItemCount(): Int = dataList.size

    fun getItem(position: Int): Data = dataList[position]

    fun data(): MutableList<Data> = dataList

    override fun getItemViewType(position: Int): Int {
        return if (position == (dataList.size - 1)) ViewType.VIEW_PROGRESS.ordinal else ViewType.VIEW_ITEM.ordinal
    }

    abstract fun getViewHolder(viewGroup: ViewGroup, viewType: Int): VH

    abstract fun getLoadingViewHolder(viewGroup: ViewGroup, viewType: Int): LH

    abstract fun bind(holder: RecyclerView.ViewHolder, data: Data, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.VIEW_ITEM.ordinal -> getViewHolder(parent, viewType)
            ViewType.VIEW_PROGRESS.ordinal -> getLoadingViewHolder(parent, viewType)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.VIEW_ITEM.ordinal -> bind(holder, dataList[position], position)
            ViewType.VIEW_PROGRESS.ordinal -> bind(
                holder = holder,
                data = dataList[position],
                position
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: List<Data>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun addItem(data: Data) {
        dataList.add(data)
        notifyItemInserted(dataList.size - 1)
    }

    fun addItems(dataList: List<Data>) {
        val oldSize = this.dataList.size
        this.dataList.addAll(dataList)
        notifyItemRangeInserted(oldSize, dataList.size)
    }

    fun addItem(position: Int, data: Data) {
        dataList.add(position, data)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun showLog(message: String) {
        Log.i(javaClass::class.java.simpleName, message)
    }
}
