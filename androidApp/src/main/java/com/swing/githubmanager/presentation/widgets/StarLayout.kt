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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class StarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val star = Path()
    private val paint = Paint()

    init {
        paint.color = 0xFF000000.toInt()
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        val clipPath = Path()
        clipPath.addPath(drawStar(canvas!!))
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    private fun drawStar(canvas: Canvas): Path {
        val midX = width / 2
        val midY = height / 2

        star.moveTo(midX.toFloat(), 0f)
        star.lineTo(midX + 20f, midY - 50f)
        star.lineTo(midX + 70f, midY - 50f)
        star.lineTo(midX + 30f, midY + 10f)
        star.lineTo(midX + 50f, midY + 60f)
        star.lineTo(midX.toFloat(), midY + 30f)
        star.lineTo(midX - 50f, midY + 60f)
        star.lineTo(midX - 30f, midY + 10f)
        star.lineTo(midX - 70f, midY - 50f)
        star.lineTo(midX - 20f, midY - 50f)

        return star
    }
}
