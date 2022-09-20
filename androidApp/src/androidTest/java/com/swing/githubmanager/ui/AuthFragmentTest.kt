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

package com.swing.githubmanager.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.swing.githubmanager.R
import com.swing.githubmanager.di.SharedPreferencesModule
import com.swing.githubmanager.presentation.dashboard.DashboardActivity
import com.swing.githubmanager.presentation.dashboard.fragments.AuthFragment
import com.swing.githubmanager.presentation.dashboard.vm.DashboardActivityVM
import com.swing.githubmanager.presentation.dashboard.vm.DashboardTab
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@UninstallModules(SharedPreferencesModule::class)
@RunWith(AndroidJUnit4::class)
class AuthFragmentTest {

    @get:Rule(order = 0)
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val activityTest = ActivityScenarioRule(DashboardActivity::class.java)

    private lateinit var dashboardActivityVM: DashboardActivityVM

    @Before
    fun setUp() {
        hiltRule.inject()
        dashboardActivityVM = DashboardActivityVM()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Truth.assertThat("com.swing.githubmanager.debug").isEqualTo(appContext.packageName)
    }

    @Test
    fun fromStartPage_nextPageRecyclerView() {
        launchFragmentInContainer<AuthFragment>().apply {
            onView(withId(R.id.signUpAuthBtn)).perform(click())
        }
    }
}
