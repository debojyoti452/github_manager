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

package com.swing.githubmanager.core.abstraction

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.swing.githubmanager.core.log.Log
import com.swing.githubmanager.core.utils.AppConstants.NIGHT_MODE_KEY
import com.swing.githubmanager.domain.preferences.GMPreferenceInterface
import javax.inject.Inject

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity(), BaseInterface {

    @Inject
    lateinit var gmPreference: GMPreferenceInterface

    lateinit var binding: T

    open fun vmObserver() {}

    abstract fun onCreated(savedInstanceState: Bundle?)

    abstract fun getViewBinding(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setOnExitAnimationListener {
            it.remove()
        }
        binding = getViewBinding()
        setContentView(binding.root)
        onCreated(savedInstanceState)
        vmObserver()
    }

    override fun showLog(msg: Any) {
        when (msg) {
            is String -> {
                Log.d(javaClass.simpleName, msg = msg)
            }
            is Boolean -> {
                Log.d(javaClass.simpleName, msg = msg.toString())
            }
            is Int -> {
                Log.d(javaClass.simpleName, msg = msg.toString())
            }
            is Throwable -> {
                Log.d(javaClass.simpleName, throwable = msg)
            }
            else -> {
                Log.d(javaClass.simpleName, msg = "Your message is not from the earth.")
            }
        }
    }

    override fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideSystemUI(layout: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            layout
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun checkAppThemeMode() {
        when (isNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun internalRestart() {
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }

    private fun isNightMode(): Int {
        return gmPreference.getPreference(NIGHT_MODE_KEY, 1) as Int
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
}
