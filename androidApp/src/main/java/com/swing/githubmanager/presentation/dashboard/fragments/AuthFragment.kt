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

package com.swing.githubmanager.presentation.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.swing.githubloginsdk.GithubAuth
import com.swing.githubloginsdk.src.constants.Scopes
import com.swing.githubmanager.BuildConfig
import com.swing.githubmanager.core.abstraction.BaseFragment
import com.swing.githubmanager.databinding.FragmentAuthBinding
import com.swing.githubmanager.presentation.dashboard.vm.DashboardActivityVM
import com.swing.githubmanager.presentation.dashboard.vm.DashboardTab

class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    private val githubAuth: GithubAuth by lazy {
        GithubAuth.Builder(BuildConfig.GIT_CLIENT_ID, BuildConfig.GIT_CLIENT_SECRET)
            .setActivity(requireActivity())
            .setOnSuccess {

            }
            .setOnFailed {

            }
            .setScopes(listOf(Scopes.PUBLIC_REPO, Scopes.USER_EMAIL))
            .build()
    }

    private val dashboardActivityVM by activityViewModels<DashboardActivityVM>()

    override fun initialized(view: View, savedInstanceState: Bundle?) {
        dashboardActivityVM.setCurrentScreen(DashboardTab.AUTH)
    }

    override fun vmObserver() {

    }

    override fun onClickListenerInit() {
        binding.signUpAuthBtn.setOnClickListener {
            dashboardActivityVM.whetherToReloadTheApi(limit = true)
            val directions = AuthFragmentDirections.actionAuthFragmentToTrendingFragment(true)
            findNavController().navigate(directions)
        }
    }

    override fun onDeviceBack() {

    }

    override fun onResume() {
        super.onResume()
        githubAuth.onDeepLinkInitializer()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentAuthBinding =
        FragmentAuthBinding.inflate(layoutInflater)

}
