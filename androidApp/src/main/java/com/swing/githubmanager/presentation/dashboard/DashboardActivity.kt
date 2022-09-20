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

package com.swing.githubmanager.presentation.dashboard

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.swing.githubmanager.R
import com.swing.githubmanager.core.extensions.Extensions.observe
import com.swing.githubmanager.databinding.ActivityDashboardBinding
import com.swing.githubmanager.core.abstraction.BaseActivity
import com.swing.githubmanager.core.extensions.Extensions.showView
import com.swing.githubmanager.presentation.dashboard.vm.DashboardActivityVM
import com.swing.githubmanager.presentation.dashboard.vm.DashboardTab
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {
    private var navController: NavController? = null
    private val dashboardActivityVM: DashboardActivityVM by viewModels()

    override fun onCreated(savedInstanceState: Bundle?) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.dashboardFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun vmObserver() {
        observe(dashboardActivityVM.currentScreen, ::observeScreen)
        super.vmObserver()
    }

    private fun observeScreen(dashboardTab: DashboardTab?) {
        dashboardTab?.apply {
            if (this == DashboardTab.TRENDING) {
                binding.dashboardToolbar.showView(state = true)
            } else {
                binding.dashboardToolbar.showView(state = false)
            }
        }
    }

    override fun getViewBinding(): ActivityDashboardBinding =
        ActivityDashboardBinding.inflate(layoutInflater)
}
