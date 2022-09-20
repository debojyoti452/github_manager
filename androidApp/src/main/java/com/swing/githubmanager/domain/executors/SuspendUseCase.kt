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

package com.swing.githubmanager.domain.executors

import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in Params, out T>(
    private val executionThread: PostExecutorThread
) {

    suspend operator fun invoke(params: Params? = null): T {
        return withContext(executionThread.io) {
            execute(params)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: Params?): T
}
