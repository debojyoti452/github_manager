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

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swing.githubmanager.R
import com.swing.githubmanager.core.abstraction.BaseFragment
import com.swing.githubmanager.core.abstraction.GMResource
import com.swing.githubmanager.core.extensions.Extensions.getNavigationResultLiveData
import com.swing.githubmanager.core.extensions.Extensions.observe
import com.swing.githubmanager.core.extensions.Extensions.showView
import com.swing.githubmanager.core.utils.AppConstants.SHARED_NAVIGATION_KEY
import com.swing.githubmanager.data.models.Params
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.databinding.FragmentTrendingBinding
import com.swing.githubmanager.presentation.adapter.TrendingAdapter
import com.swing.githubmanager.presentation.dashboard.vm.DashboardActivityVM
import com.swing.githubmanager.presentation.dashboard.vm.DashboardTab
import com.swing.githubmanager.presentation.dashboard.vm.DetailsVM
import com.swing.githubmanager.presentation.dashboard.vm.TrendingVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendingFragment : BaseFragment<FragmentTrendingBinding>(), View.OnClickListener {

    private val trendingVm by viewModels<TrendingVM>()
    private val detailsVm by activityViewModels<DetailsVM>()
    private val dashboardVm by activityViewModels<DashboardActivityVM>()

    private val trendingAdapter by lazy {
        TrendingAdapter(onItemClicked = { it, viewTriple ->
            detailsVm.setDetailsModel(it)
            val animExtras = FragmentNavigatorExtras(
                (viewTriple.first as View) to "${it.owner?.avatarUrl}",
                (viewTriple.second as View) to "${it.description}",
                (viewTriple.third as View) to "${it.fullName}"
            )

            findNavController().navigate(
                R.id.action_trendingFragment_to_detailsFragment,
                null,
                null,
                animExtras
            )
        })
    }

    // Initialized
    override fun initialized(view: View, savedInstanceState: Bundle?) {
        dashboardVm.setCurrentScreen(DashboardTab.TRENDING)
        // When user hits back button transition takes backward
        postponeEnterTransition()
        binding.rvTrending.doOnPreDraw {
            startPostponedEnterTransition()
        }
        setupRecyclerView()
    }

    // set up recycler view
    private fun setupRecyclerView() {
        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = trendingAdapter
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                ).apply {
                    ContextCompat.getDrawable(requireContext(), R.drawable.divider)
                        ?.let { setDrawable(it) }
                }
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        trendingVm.getTrendingList(
                            trendingVm.trendingParams.value!!.copy(
                                page = trendingVm.trendingParams.value!!.page + 1,
                                onLoadMode = true,
                            )
                        )
                    }
                }
            })
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onClickListenerInit() {
        binding.filterButton.setOnClickListener(this)
        binding.trendingSearchTextInputEditText.apply {
            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    transitionToSearchScreen()
                }
                false
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    transitionToSearchScreen()
                }
            }
        }
    }

    // Prebuilt function to ViewModel Observers
    override fun vmObserver() {
        observe(trendingVm.trendingList, ::getTrendingList)
        observe(trendingVm.trendingParams, ::getParamsChange)
        observe(dashboardVm.whetherToReload, ::loadDataForFirstTime)

        val result = getNavigationResultLiveData<String>(SHARED_NAVIGATION_KEY)
        result?.observe(viewLifecycleOwner) {
            getSearchText(it)
        }
    }

    // observe to load data for first time
    private fun loadDataForFirstTime(value: Boolean?) {
        value?.let {
            if (it) {
                trendingVm.getTrendingList(trendingVm.trendingParams.value!!)
            }
        }
    }

    // Observe Text Search
    private fun getSearchText(text: String?) {
        text?.let {
            if (text.isNotEmpty()) {
                binding.trendingSearchTextInputEditText.setText(it)
                trendingVm.getTrendingList(
                    trendingVm.trendingParams.value!!.copy(
                        query = it,
                        order = "desc",
                        page = 1,
                        onLoadMode = false,
                    )
                )
            }
        }
    }

    // Observe Params Change
    private fun getParamsChange(params: Params?) {
        params?.let {

        }
    }

    // Observe Trending List
    private fun getTrendingList(gmResource: GMResource<List<TrendingDataModel>>?) {
        when (gmResource) {
            is GMResource.Success -> {
                if (trendingVm.trendingParams.value?.onLoadMode == true) {
                    trendingAdapter.addItems(gmResource.data)
                } else {
                    trendingAdapter.setData(gmResource.data)
                }

                binding.rvTrendingProgressBar.showView(false)
            }
            is GMResource.Error -> {
                binding.rvTrendingProgressBar.showView(false)
                showLog("Error: ${gmResource.exception}")
            }
            is GMResource.Loading -> {
                if (trendingVm.trendingParams.value?.page == 1) {
                    binding.rvTrendingProgressBar.showView(true)
                } else {
                    binding.rvTrendingProgressBar.showView(false)
                }
                showLog("Loading")
            }
            else -> {
                binding.rvTrendingProgressBar.showView(false)
                showLog("Unknown ${gmResource.toString()}")
            }
        }
    }

    // Transition To Back Screen
    override fun onDeviceBack() {

    }

    // OnClick Listener
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.filterButton -> {
                val bottomSheetFragment =
                    FilterBottomSheetFragment(onResponseListener = { pairedData ->
                        binding.trendingSearchTextInputEditText.setText("")
                        binding.rvTrendingProgressBar.showView(true)
                        trendingVm.getTrendingList(
                            trendingVm.trendingParams.value!!.copy(
                                query = pairedData.first.ifEmpty { trendingVm.trendingParams.value!!.query },
                                order = pairedData.second,
                                page = 1,
                                onLoadMode = false,
                            )
                        )
                    })
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
            }
        }
    }

    // Transition To Search Screen
    private fun transitionToSearchScreen() {
        binding.trendingSearchTextInputEditText.transitionName = SHARED_NAVIGATION_KEY

        val animExtras = FragmentNavigatorExtras(
            binding.trendingSearchTextInputEditText to SHARED_NAVIGATION_KEY,
        )

        findNavController().navigate(
            R.id.action_trendingFragment_to_searchFragment,
            null,
            null,
            animExtras
        )
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrendingBinding {
        sharedElementReturnTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        return FragmentTrendingBinding.inflate(inflater, container, false)
    }
}
