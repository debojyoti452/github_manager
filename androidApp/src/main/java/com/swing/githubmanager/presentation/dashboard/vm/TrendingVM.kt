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

package com.swing.githubmanager.presentation.dashboard.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swing.githubmanager.core.abstraction.GMResource
import com.swing.githubmanager.data.models.Params
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.domain.preferences.GMPreferenceInterface
import com.swing.githubmanager.domain.usecases.LanguageUseCase
import com.swing.githubmanager.domain.usecases.TrendingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingVM @Inject constructor(
    private val gmPreferenceInterface: GMPreferenceInterface,
    private val trendingUseCase: TrendingUseCase,
    private val languageUseCase: LanguageUseCase
) : ViewModel() {

    private val _trendingList: MutableLiveData<GMResource<MutableList<TrendingDataModel>>> =
        MutableLiveData()
    val trendingList: LiveData<GMResource<MutableList<TrendingDataModel>>> = _trendingList

    private val _trendingListByLanguage: MutableLiveData<GMResource<Map<String, Int>>> =
        MutableLiveData()
    val trendingListByLanguage: LiveData<GMResource<Map<String, Int>>> =
        _trendingListByLanguage

    private val _trendingParams: MutableLiveData<Params> = MutableLiveData(Params())
    val trendingParams: LiveData<Params> = _trendingParams

    // CoroutineExceptionHandler to handle exceptions.
    private val _trendingCoroutineException = CoroutineExceptionHandler { _, throwable ->
        _trendingList.value = GMResource.Error(throwable.message)
    }

    private val _languageCoroutineException = CoroutineExceptionHandler { _, throwable ->
        _trendingListByLanguage.value = GMResource.Error(throwable.message)
    }


    // Get trending list from API
    fun getTrendingList(params: Params) {
        _trendingParams.value = params
        _trendingList.value = GMResource.Loading
        viewModelScope.launch(_trendingCoroutineException) {
            val result = trendingUseCase.invoke(params)
            _trendingList.value = GMResource.Success(result)
        }
    }

    // Get trending list by language from API
    fun getTrendingListByLanguage(name: String) {
        _trendingListByLanguage.value = GMResource.Loading
        viewModelScope.launch(_languageCoroutineException) {
            val result = languageUseCase.invoke(params = name)
            _trendingListByLanguage.value = GMResource.Success(result)
        }
    }

    // Update params
    fun updateParams(params: Params) {
        _trendingParams.value = params
    }
}
