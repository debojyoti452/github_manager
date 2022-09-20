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

package com.swing.githubmanager.data.models


import com.google.gson.annotations.SerializedName

data class TrendingDataModel(
    @SerializedName("clone_url")
    val cloneUrl: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @SerializedName("languages_url")
    val languagesUrl: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("default_branch")
    val defaultBranch: String? = null,
    @SerializedName("owner")
    val owner: Owner? = null,
    @SerializedName("stargazers_count")
    val stargazersCount: Int? = null,
    @SerializedName("topics")
    val topics: List<String>? = null,
    @SerializedName("watchers_count")
    val watchersCount: Int? = null,
    val languageMap: Map<String, Int>? = null
)
