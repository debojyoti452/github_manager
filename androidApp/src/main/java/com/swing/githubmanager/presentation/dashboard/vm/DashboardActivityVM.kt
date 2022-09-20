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

enum class DashboardTab {
    AUTH,
    TRENDING,
    SEARCH,
    DETAILS,
}

class DashboardActivityVM : ViewModel() {
    private val _currentScreen = MutableLiveData(DashboardTab.AUTH)
    val currentScreen: LiveData<DashboardTab> = _currentScreen

    private val _whetherToReload: MutableLiveData<Boolean> = MutableLiveData(false)
    val whetherToReload: LiveData<Boolean> = _whetherToReload

    fun setCurrentScreen(screen: DashboardTab) {
        _currentScreen.value = screen
    }

    fun whetherToReloadTheApi(limit: Boolean) {
        _whetherToReload.value = limit
    }
}
