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

package com.swing.githubmanager.domain.preferences

import android.content.Context
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.swing.githubmanager.core.utils.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext

class GMPreference constructor(
    @ApplicationContext context: Context
) : GMPreferenceInterface {

    private val sharedPreferences =
        context.getSharedPreferences(AppConstants.SECURE_PREF, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T =
        when (T::class) {
            String::class -> getString(key, defaultValue as? String ?: "") as T
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
            else -> throw UnsupportedOperationException("Only primitive types can be read from SharedPreferences")
        }

    private fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        when (val value = pair.second) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }

    override fun getPreference(key: String, default: Any): Any {
        return when (default) {
            is String -> sharedPreferences.getString(key, default) ?: ""
            is Int -> sharedPreferences.getInt(key, default)
            is Boolean -> sharedPreferences.getBoolean(key, default)
            is Long -> sharedPreferences.getLong(key, default)
            is Float -> sharedPreferences.getFloat(key, default)
            else -> throw UnsupportedOperationException("Only primitive types can be read from SharedPreferences")
        }
    }

    override fun setPreference(key: String, value: Any) {
        sharedPreferences.editMe {
            it.put(key to value)
        }
    }

    override fun removePreference(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.get(AppConstants.NIGHT_MODE_KEY, 1) == 1
    }
}
