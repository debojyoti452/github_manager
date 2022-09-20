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

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.swing.githubmanager.R
import com.swing.githubmanager.data.local.ConstantStorage
import com.swing.githubmanager.databinding.FragmentFilterSheetBinding
import kotlin.random.Random

class FilterBottomSheetFragment constructor(
    val onResponseListener: (Pair<String, String>) -> Unit
) : BottomSheetDialogFragment() {

    private var binding: FragmentFilterSheetBinding? = null
    private val selectedLanguage: BooleanArray =
        BooleanArray(ConstantStorage.programmingTempData.size)
    private val selectedLangList = ArrayList<Int>()
    private var selectedSort: String = "desc"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showOrderDropdown()
        binding!!.filterLanguageTV.setOnClickListener {
            showLanguageDialog()
        }

        binding!!.applyFilterButton.setOnClickListener {
            if (selectedLangList.size > 0) {
                onResponseListener(
                    Pair(
                        ConstantStorage.programmingTempData[selectedLangList.random(
                            Random(
                                selectedLangList.size
                            )
                        )],
                        selectedSort
                    )
                )
                dismiss()
            } else {
                onResponseListener(
                    Pair(
                        "",
                        selectedSort
                    )
                )
                dismiss()
            }
        }
    }

    private fun showLanguageDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        builder.setTitle("Select Language")
        builder.setMultiChoiceItems(
            ConstantStorage.programmingTempData.toTypedArray(),
            selectedLanguage
        ) { _, which, isChecked ->
            if (isChecked) {
                selectedLangList.add(which)
                selectedLangList.sort()
            } else {
                selectedLangList.remove(which)
            }
        }

        builder.setPositiveButton("OK") { _, _ ->
            val selectedLang = StringBuilder()
            for (i in selectedLangList.indices) {
                selectedLang.append(ConstantStorage.programmingTempData[selectedLangList[i]])
                if (i != selectedLangList.size - 1) {
                    selectedLang.append(", ")
                }
            }
            binding?.filterLanguageTV?.text = selectedLang.toString()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setNeutralButton("Clear All") { _, _ ->
            for (i in selectedLanguage.indices) {
                selectedLanguage[i] = false
                selectedLangList.clear()
                binding?.filterLanguageTV?.text = getString(R.string.select_language)
            }
        }

        builder.show()
    }

    private fun showOrderDropdown() {
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            ConstantStorage.starsOrderTempData
        )
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)

        binding!!.filterStarsTV.apply {
            adapter = spinnerAdapter
            setSelection(0, true)
            gravity = Gravity.CENTER
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedSort = if (position == 0) {
                        "desc"
                    } else {
                        "asc"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterSheetBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialog

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
