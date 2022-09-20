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

import com.swing.githubmanager.core.abstraction.BaseViewHolder
import com.swing.githubmanager.core.extensions.Extensions.showView
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.databinding.LayoutLoadingViewBinding

class LoadingViewHolder(private val binding: LayoutLoadingViewBinding) :
    BaseViewHolder<TrendingDataModel>(binding) {
    override fun bindView(item: TrendingDataModel, position: Int) {
        binding.progressBar.showView(state = true)
    }
}
