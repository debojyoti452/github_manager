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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.icu.text.CompactDecimalFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import coil.load
import coil.transform.CircleCropTransformation
import com.swing.githubmanager.R
import com.swing.githubmanager.core.abstraction.BaseFragment
import com.swing.githubmanager.core.extensions.Extensions
import com.swing.githubmanager.core.extensions.Extensions.observe
import com.swing.githubmanager.core.extensions.Extensions.setNavigationResult
import com.swing.githubmanager.core.extensions.Extensions.showView
import com.swing.githubmanager.core.utils.AppConstants.SHARED_NAVIGATION_KEY
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.databinding.FragmentDetailsBinding
import com.swing.githubmanager.presentation.dashboard.vm.DashboardActivityVM
import com.swing.githubmanager.presentation.dashboard.vm.DashboardTab
import com.swing.githubmanager.presentation.dashboard.vm.DetailsVM
import es.dmoral.markdownview.MarkdownView
import java.util.*
import java.util.concurrent.TimeUnit


class DetailsFragment : BaseFragment<FragmentDetailsBinding>(), View.OnClickListener {

    private val detailsVM by activityViewModels<DetailsVM>()
    private val dashboardVm by activityViewModels<DashboardActivityVM>()


    override fun initialized(view: View, savedInstanceState: Bundle?) {
        dashboardVm.setCurrentScreen(DashboardTab.DETAILS)
    }

    // ViewModel Observers
    override fun vmObserver() {
        observe(detailsVM.detailsModel, ::onDetailsModel)
    }

    private fun onDetailsModel(data: TrendingDataModel?) {
        data?.let { item ->
            item.languageMap?.let {
                if (it.isNotEmpty()) {
                    binding.itemTrendingLangTV.showView(state = true)
                    binding.itemTrendingLangProgressView.showView(state = true)
                    binding.itemTrendingLangProgressView.setData(it)
                } else {
                    binding.itemTrendingLangTV.showView(state = false)
                    binding.itemTrendingLangProgressView.showView(state = false)
                }
            }

            binding.itemTrendingIV.load(item.owner?.avatarUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(Extensions.showCircularLoading(binding.root.context))
            }

            val stargazersCountKotlin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CompactDecimalFormat.getInstance(
                    Locale.getDefault(),
                    CompactDecimalFormat.CompactStyle.SHORT
                )
                    .format(item.stargazersCount ?: 0)
            } else {
                item.stargazersCount ?: 0
            }

            val watchersCountKotlin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CompactDecimalFormat.getInstance(
                    Locale.getDefault(),
                    CompactDecimalFormat.CompactStyle.SHORT
                )
                    .format(item.watchersCount ?: 0)
            } else {
                item.watchersCount ?: 0
            }

            binding.itemTrendingNameTV.text = item.owner?.login
            binding.itemTrendingRepoTV.text = item.fullName
            binding.itemTrendingDescTV.text = item.description
            binding.itemTrendingRepoLinkTV.text = item.cloneUrl
            binding.itemTrendingStarsTV.text = "$stargazersCountKotlin"
            binding.itemTrendingForksTV.text = "$watchersCountKotlin"

            binding.itemTrendingIV.transitionName = item.owner?.avatarUrl ?: SHARED_NAVIGATION_KEY
            binding.itemTrendingDescTV.transitionName = item.description ?: SHARED_NAVIGATION_KEY
            binding.itemTrendingLangProgressView.transitionName =
                item.fullName ?: SHARED_NAVIGATION_KEY

            setupMarkdownView(item)
        }
    }

    // copy repo link to clipboard
    private fun copyRepoLink() {
        val clipboardManager =
            requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("repo_link", binding.itemTrendingRepoLinkTV.text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    // MARK: - MarkdownView
    private fun setupMarkdownView(item: TrendingDataModel) {
        binding.markdownViewProgress.visibility = View.VISIBLE
        binding.markdownView.apply {
            loadFromUrl("https://raw.githubusercontent.com/${item.fullName}/${item.defaultBranch}/README.md")

            setOnMarkdownRenderingListener(object : MarkdownView.OnMarkdownRenderingListener {
                override fun onMarkdownFinishedRendering() {
                    binding.markdownViewProgress.visibility = View.GONE
                }

                override fun onMarkdownRenderError() {
                    binding.markdownViewProgress.visibility = View.GONE
                }
            })
        }
    }

    override fun onClickListenerInit() {
        binding.backButton.setOnClickListener(this)
        binding.itemTrendingRepoLinkTV.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.backButton -> {
                onDeviceBack()
            }
            R.id.itemTrendingRepoLinkTV -> {
                copyRepoLink()
            }
        }
    }

    override fun onDeviceBack() {
        dashboardVm.whetherToReloadTheApi(limit = false)
        setNavigationResult(
            "",
            SHARED_NAVIGATION_KEY,
        )
        findNavController().navigateUp()
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(400, TimeUnit.MILLISECONDS)
        return FragmentDetailsBinding.inflate(inflater, container, false)
    }
}
