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

package com.swing.githubmanager.presentation.adapter.viewholder

import android.icu.text.CompactDecimalFormat
import android.os.Build
import coil.load
import coil.transform.CircleCropTransformation
import com.swing.githubmanager.core.abstraction.BaseViewHolder
import com.swing.githubmanager.core.extensions.Extensions.showCircularLoading
import com.swing.githubmanager.core.extensions.Extensions.showView
import com.swing.githubmanager.core.utils.AppConstants
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.databinding.LayoutTrendingItemBinding
import java.util.*

class TrendingViewHolder(val binding: LayoutTrendingItemBinding) :
    BaseViewHolder<TrendingDataModel>(binding) {
    override fun bindView(item: TrendingDataModel, position: Int) {
        binding.itemTrendingIV.load(item.owner?.avatarUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(showCircularLoading(binding.root.context))
        }

        binding.itemTrendingTV.text = item.name
        binding.itemTrendingDescTV.text = item.description
        binding.itemTrendingUserNameTV.text = item.owner?.login

        val stargazersCountKotlin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CompactDecimalFormat.getInstance(
                Locale.getDefault(),
                CompactDecimalFormat.CompactStyle.SHORT
            )
                .format(item.stargazersCount ?: 0)
        } else {
            item.stargazersCount ?: 0
        }

        val watchersCountKotlin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CompactDecimalFormat.getInstance(
                Locale.getDefault(),
                CompactDecimalFormat.CompactStyle.SHORT
            )
                .format(item.watchersCount ?: 0)
        } else {
            item.watchersCount ?: 0
        }

        binding.itemStarsCountTV.text = "$stargazersCountKotlin"
        binding.itemForksCountTV.text = "$watchersCountKotlin"

        item.languageMap?.let {
            if (it.isNotEmpty()) {
                binding.itemTrendingLangTV.showView(state = true)
                binding.itemLanguageGraphView.showView(state = true)
                binding.itemLanguageGraphView.setData(it)
            } else {
                binding.itemTrendingLangTV.showView(state = false)
                binding.itemLanguageGraphView.showView(state = false)
            }
        }

        // setting shared transition name
        binding.itemTrendingIV.transitionName =
            item.owner?.avatarUrl ?: AppConstants.SHARED_NAVIGATION_KEY
        binding.itemTrendingDescTV.transitionName =
            item.description ?: AppConstants.SHARED_NAVIGATION_KEY
        binding.itemLanguageGraphView.transitionName =
            item.fullName ?: AppConstants.SHARED_NAVIGATION_KEY

        // rotate animation in circular mode
        binding.itemTrendingIV.apply {
            rotation = 0f
            animate().rotationBy(360f).setDuration(1000).start()
        }
    }
}
