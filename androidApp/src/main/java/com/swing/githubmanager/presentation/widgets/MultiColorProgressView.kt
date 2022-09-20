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

package com.swing.githubmanager.presentation.widgets

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.swing.githubmanager.R
import com.swing.githubmanager.core.utils.PixelUtils
import com.swing.githubmanager.data.local.ConstantStorage

class MultiColorProgressView : LinearLayoutCompat {

    private var count = 0
    private var total = 0
    private var dataMap = mutableMapOf<String, Int>()
    private val limit = 5

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        removeAllViews()
        setWillNotDraw(false)
        orientation = VERTICAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        if (!isInEditMode) {
            ConstantStorage.progressBarColorArray.apply {
                shuffle()
                reversed()
            }

            val progressLayout = LinearLayoutCompat(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    PixelUtils.getPixels(context, 10f)
                )
                setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            }

            val programmingLayout = LinearLayoutCompat(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                )
            }

            addProgressView(progressLayout)
            addProgrammingView(programmingLayout)
        }
    }

    private fun addProgrammingView(programmingLayout: LinearLayoutCompat) {
        dataMap.asIterable().take(limit).forEachIndexed { index, (key, value) ->
            val view = LinearLayoutCompat(context).apply {
                layoutParams = LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    1f
                ).apply {
                    setMargins(
                        0,
                        PixelUtils.getPixels(context, 4f),
                        0,
                        0
                    )
                }
                orientation = HORIZONTAL
            }

            val textView = TextView(context).apply {
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 0)
                    setPadding(
                        PixelUtils.getPixels(context, 4f),
                        0,
                        PixelUtils.getPixels(context, 2f),
                        0
                    )
                    gravity = Gravity.CENTER_VERTICAL
                    setHorizontalGravity(CENTER)
                }
                id = index
                setTextColor(ContextCompat.getColor(context, R.color.secondaryTextColor))
                textSize = 14f
                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                setLines(1)
                ellipsize = TextUtils.TruncateAt.END
                text = key
            }

            val boxCustomView = View(context).apply {
                layoutParams = LayoutParams(
                    PixelUtils.getPixels(context, 10f),
                    PixelUtils.getPixels(context, 10f),
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
                setBackgroundColor(ConstantStorage.progressBarColorArray[index])
            }

            view.addView(boxCustomView)
            view.addView(textView)

            programmingLayout.addView(view)
        }
        addView(programmingLayout)
    }


    private fun addProgressView(progressLayout: LinearLayoutCompat) {
        dataMap.asIterable().take(limit).forEachIndexed { index, (_, value) ->
            val colorPicked = ConstantStorage.progressBarColorArray[index]
            val progressView = LinearLayoutCompat(context)
            val layoutParams = LayoutParams(
                0, LayoutParams.MATCH_PARENT,
                PixelUtils.getPixels(context = context, value).toFloat()
            )
            layoutParams.setMargins(0, 0, PixelUtils.getPixels(context = context, 0.6f), 0)
            progressView.setBackgroundColor(colorPicked)
            progressView.layoutParams = layoutParams
            progressLayout.addView(progressView)
        }

        addView(progressLayout)
    }

    fun setData(data: Map<String, Int>) {
        dataMap.clear()
        dataMap.putAll(data)
        count = dataMap.size
        total = dataMap.asIterable().sumOf { it.value }
        init(context, null)
        invalidate()
    }
}
