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

package com.swing.githubmanager.data.impl

import com.swing.githubmanager.core.log.Log
import com.swing.githubmanager.data.models.Params
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.data.service.GMApiService
import com.swing.githubmanager.data.utils.BaseModel
import com.swing.githubmanager.data.utils.CustomException
import com.swing.githubmanager.data.utils.GMExceptionHandler
import com.swing.githubmanager.domain.repository.GMRepository
import javax.inject.Inject

class GMRemoteImpl @Inject constructor(
    private val service: GMApiService
) : GMRepository {
    override suspend fun getTrendingList(params: Params): MutableList<TrendingDataModel> {
        val queryMap = mapOf(
            "q" to params.query,
            "sort" to params.sort,
            "order" to params.order,
            "page" to params.page.toString(),
            "per_page" to params.perPage.toString(),
        )

        val result = runCatching { service.getTrendingList(queryMap) }

        if (result.isFailure) {
            result.exceptionOrNull()?.let {
                Log.e(GMRemoteImpl::class.java.simpleName, it)
                val errMessage = GMExceptionHandler.extractErrorMessage(it)
                throw CustomException(errMessage)
            }
        }

        val response: BaseModel<List<TrendingDataModel>>? = result.getOrNull()

        val returnResponse = mutableListOf<TrendingDataModel>()

        response?.let {
            it.items.forEach { item ->
                val languageMap = item.languagesUrl?.let { it1 -> getLanguageData(it1) }

                returnResponse.add(
                    TrendingDataModel(
                        cloneUrl = item.cloneUrl,
                        description = item.description,
                        fullName = item.fullName,
                        htmlUrl = item.htmlUrl,
                        languagesUrl = item.languagesUrl,
                        name = item.name,
                        owner = item.owner,
                        stargazersCount = item.stargazersCount,
                        topics = item.topics,
                        watchersCount = item.watchersCount,
                        languageMap = languageMap,
                        defaultBranch = item.defaultBranch,
                    )
                )
            }
        }

        return returnResponse
    }

    override suspend fun getLanguageData(name: String): Map<String, Int> {
        val result = runCatching { service.getLanguageData(name = name) }

        if (result.isFailure) {
            result.exceptionOrNull()?.let {
                val errMessage = GMExceptionHandler.extractErrorMessage(it)
                throw CustomException(errMessage)
            }
        }

        val response: Map<String, Int>? = result.getOrNull()

        response?.let {
            return it
        }

        return emptyMap()
    }
}
