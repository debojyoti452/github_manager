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

package com.swing.githubmanager.domain.usecases

import com.swing.githubmanager.data.models.Params
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.domain.executors.PostExecutorThread
import com.swing.githubmanager.domain.executors.SuspendUseCase
import com.swing.githubmanager.domain.repository.GMRepository
import javax.inject.Inject

class TrendingUseCase @Inject constructor(
    private val trendingRepository: GMRepository,
    postExecution: PostExecutorThread
) : SuspendUseCase<Params, MutableList<TrendingDataModel>>(postExecution) {
    override suspend fun execute(params: Params?): MutableList<TrendingDataModel> {
        return trendingRepository.getTrendingList(
            params = params ?: Params()
        )
    }
}

class LanguageUseCase @Inject constructor(
    private val trendingRepository: GMRepository,
    postExecution: PostExecutorThread
) : SuspendUseCase<String, Map<String, Int>>(postExecution) {
    override suspend fun execute(params: String?): Map<String, Int> {
        requireNotNull(params) { "Language name cannot be null" }
        return trendingRepository.getLanguageData(
            params
        )
    }
}
