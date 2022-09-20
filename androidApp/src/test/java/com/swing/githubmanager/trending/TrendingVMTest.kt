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

package com.swing.githubmanager.trending

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.swing.githubmanager.core.abstraction.GMResource
import com.swing.githubmanager.data.models.Owner
import com.swing.githubmanager.data.models.Params
import com.swing.githubmanager.data.models.TrendingDataModel
import com.swing.githubmanager.domain.preferences.GMPreferenceInterface
import com.swing.githubmanager.domain.usecases.LanguageUseCase
import com.swing.githubmanager.domain.usecases.TrendingUseCase
import com.swing.githubmanager.presentation.dashboard.vm.TrendingVM
import com.swing.githubmanager.util.CoroutineDispatcherRule
import com.swing.githubmanager.util.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class TrendingVMTest {

    @get:Rule
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = CoroutineDispatcherRule()

    private lateinit var trendingVM: TrendingVM

    @MockK
    lateinit var trendingUseCase: TrendingUseCase

    @MockK
    lateinit var languageUseCase: LanguageUseCase

    @MockK
    lateinit var preference: GMPreferenceInterface

    private val params = Params()

    private val positiveResponse = mutableListOf(
        TrendingDataModel(
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
    )

    private val sortedResponse = mutableListOf(
        TrendingDataModel(
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
        ),
        TrendingDataModel(
            cloneUrl = "https://github.com/debojyoti452/dart_std.git",
            description = "A Open Source Library.",
            fullName = "debojyoti452/dart_std",
            htmlUrl = "https://github.com/debojyoti452/dart_std",
            languagesUrl = "https://api.github.com/repos/debojyoti452/dart_std/languages",
            name = "dart_std",
            defaultBranch = "main",
            stargazersCount = 20,
            watchersCount = 200,
            topics = listOf("dart", "flutter", "dart-std"),
            owner = Owner(
                avatarUrl = "https://avatars.githubusercontent.com/u/20729878?v=4",
                login = "debojyoti452",
                htmlUrl = "https://github.com/debojyoti452",
                nodeId = "MDQ6VXNlcjIwNzI5ODc4",
                url = "https://api.github.com/users/debojyoti452",
                id = 20729878
            ),
        ),
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        trendingVM = TrendingVM(
            preference,
            trendingUseCase,
            languageUseCase
        )
    }

    @Test
    fun `test get positive trending data`() = runTest {
        val apiResponse = positiveResponse

        coEvery {
            trendingUseCase.invoke(params)
        } returns apiResponse

        trendingVM.getTrendingList(params)

        advanceUntilIdle()

        val value = trendingVM.trendingList.getOrAwaitValue()

        Truth.assertThat(value).isEqualTo(GMResource.Success(positiveResponse))
    }

    @Test
    fun `test get negative trending data`() = runTest {
        coEvery {
            trendingUseCase.invoke(params)
        } returns mutableListOf()

        trendingVM.getTrendingList(params)

        advanceUntilIdle()

        val value = trendingVM.trendingList.getOrAwaitValue()

        Truth.assertThat(value).isNotEqualTo(GMResource.Success(mutableListOf(positiveResponse)))
    }

    @Test
    fun `test get ascending sorted params trending data`() = runTest {
        val apiResponse = sortedResponse

        val sortParam = params.copy(sort = "asc")

        coEvery {
            trendingUseCase.invoke(sortParam)
        } returns apiResponse

        trendingVM.getTrendingList(sortParam)

        advanceUntilIdle()

        val value = trendingVM.trendingList.getOrAwaitValue()

        Truth.assertThat(value).isEqualTo(GMResource.Success(sortedResponse))
    }

    @Test
    fun `test get descending sorted params trending data`() = runTest {
        val apiResponse = sortedResponse.reversed().toMutableList()

        val sortParam = params.copy(sort = "desc")

        coEvery {
            trendingUseCase.invoke(sortParam)
        } returns apiResponse

        trendingVM.getTrendingList(sortParam)

        advanceUntilIdle()

        val value = trendingVM.trendingList.getOrAwaitValue()

        Truth.assertThat(value).isEqualTo(GMResource.Success(sortedResponse.reversed().toMutableList()))
    }

    @Test
    fun `search a query and get return result`() = runTest {
        val apiResponse = positiveResponse

        val query = params.copy(query = "dart_std")

        coEvery {
            trendingUseCase.invoke(query)
        } returns apiResponse

        trendingVM.getTrendingList(query)

        advanceUntilIdle()

        val value = trendingVM.trendingList.getOrAwaitValue()

        Truth.assertThat(value).isEqualTo(GMResource.Success(positiveResponse))
    }
}
