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

package com.swing.githubmanager.core.log

import android.util.Log
import androidx.annotation.MainThread
import com.swing.githubmanager.BuildConfig

object Log {

    /**
     * Will be initiate in Base class
     */
    @MainThread
    fun init() {

    }

    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg, null)
        }
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg, null)
        }
    }

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg, null)
        }
    }

    fun w(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg, null)
        }
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg, null)
        }
    }

    fun v(tag: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, null, throwable)
        }
    }

    fun d(tag: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, null, throwable)
        }
    }

    fun i(tag: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {

            Log.i(tag, null, throwable)
        }
    }

    fun w(tag: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {

            Log.w(tag, null, throwable)
        }
    }

    fun e(tag: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {

            Log.e(tag, null, throwable)
        }
    }

    private fun tag(clazz: String): String {
        return if (clazz.length > 23) {
            clazz.substring(0, 23)
        } else {
            clazz
        }
    }
}
