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
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.swing.githubmanager.core.abstraction.BaseFragment
import com.swing.githubmanager.core.extensions.Extensions.setNavigationResult
import com.swing.githubmanager.core.utils.AppConstants.SHARED_NAVIGATION_KEY
import com.swing.githubmanager.databinding.FragmentSearchBinding
import com.swing.githubmanager.presentation.dashboard.vm.DashboardActivityVM
import com.swing.githubmanager.presentation.dashboard.vm.DashboardTab
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val dashboardVm by activityViewModels<DashboardActivityVM>()

    override fun initialized(view: View, savedInstanceState: Bundle?) {
        dashboardVm.setCurrentScreen(DashboardTab.SEARCH)
        binding.searchTextInputEditText.transitionName = SHARED_NAVIGATION_KEY
        binding.searchTextInputEditText.requestFocus()
        showKeyboard(binding.searchTextInputEditText)

        binding.searchTextInputEditText.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString()
                if (query.isNotEmpty()) {
                    hideKeyboard(binding.searchTextInputEditText)
                    setNavigationResult(textView.text.toString(), SHARED_NAVIGATION_KEY)
                    onDeviceBack()
                }
                true
            } else {
                false
            }
        }
    }

    override fun vmObserver() {

    }

    override fun onClickListenerInit() {

    }

    override fun onDeviceBack() {
        dashboardVm.whetherToReloadTheApi(limit = false)
        findNavController().navigateUp()
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

}
