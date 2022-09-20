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

package com.swing.githubmanager.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.swing.githubmanager.presentation.dashboard.vm.DashboardActivityVM
import com.swing.githubmanager.presentation.dashboard.vm.DashboardTab
import com.swing.githubmanager.util.CoroutineDispatcherRule
import com.swing.githubmanager.util.getOrAwaitValue
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class DashboardActivityVMTest {

    @get:Rule
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = CoroutineDispatcherRule()

    private lateinit var dashboardActivityVM: DashboardActivityVM

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dashboardActivityVM = DashboardActivityVM()
    }

    @Test
    fun `get current screen`() = runTest {
        val current = dashboardActivityVM.currentScreen.getOrAwaitValue()
        Truth.assertThat(current).isEqualTo(DashboardTab.AUTH)

        // change the current screen
        dashboardActivityVM.setCurrentScreen(DashboardTab.TRENDING)
        val current2 = dashboardActivityVM.currentScreen.getOrAwaitValue()
        Truth.assertThat(current2).isEqualTo(DashboardTab.TRENDING)

        // change the current screen, but check negative value
        dashboardActivityVM.setCurrentScreen(DashboardTab.DETAILS)
        val current3 = dashboardActivityVM.currentScreen.getOrAwaitValue()
        Truth.assertThat(current3).isNotEqualTo(DashboardTab.TRENDING)
    }

    @Test
    fun `whether to reload api`() = runTest {
        val limit = dashboardActivityVM.whetherToReload.getOrAwaitValue()
        Truth.assertThat(limit).isEqualTo(false)

        // change the limit
        dashboardActivityVM.whetherToReloadTheApi(false)
        val limit2 = dashboardActivityVM.whetherToReload.getOrAwaitValue()
        Truth.assertThat(limit2).isEqualTo(false)

        // change the limit, but check negative value
        dashboardActivityVM.whetherToReloadTheApi(true)
        val limit3 = dashboardActivityVM.whetherToReload.getOrAwaitValue()
        Truth.assertThat(limit3).isNotEqualTo(false)
    }
}
