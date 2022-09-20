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

package com.swing.githubmanager.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.swing.githubmanager.data.models.Owner
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.presentation.dashboard.vm.DetailsVM
import com.swing.githubmanager.util.CoroutineDispatcherRule
import com.swing.githubmanager.util.getOrAwaitValue
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsVMTest {

    @get:Rule
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = CoroutineDispatcherRule()

    private lateinit var detailsVM: DetailsVM

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        detailsVM = DetailsVM()
    }

    private val positiveResponse = TrendingDataModel(
            cloneUrl = "https://github.com/debojyoti452/dart_std.git",
            description = "A Open Source Library.",
            fullName = "debojyoti452/dart_std",
            htmlUrl = "https://github.com/debojyoti452/dart_std",
            languagesUrl = "https://api.github.com/repos/debojyoti452/dart_std/languages",
            name = "dart_std",
            defaultBranch = "main",
            stargazersCount = 2,
            watchersCount = 2,
            topics = listOf("dart", "flutter", "dart-std"),
            owner = Owner(
                avatarUrl = "https://avatars.githubusercontent.com/u/20729878?v=4",
                login = "debojyoti452",
                htmlUrl = "https://github.com/debojyoti452",
                nodeId = "MDQ6VXNlcjIwNzI5ODc4",
                url = "https://api.github.com/users/debojyoti452",
                id = 20729878
            ),
        )


    @Test
    fun `test getDetails`() = runTest {
        detailsVM.setDetailsModel(positiveResponse)

        val result = detailsVM.detailsModel.getOrAwaitValue()

        Truth.assertThat(result).isEqualTo(positiveResponse)

        /// test for empty data
        detailsVM.setDetailsModel(TrendingDataModel())

        val result2 = detailsVM.detailsModel.getOrAwaitValue()

        Truth.assertThat(result2).isEqualTo(TrendingDataModel())
    }
}
