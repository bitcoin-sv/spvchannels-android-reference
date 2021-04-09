// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.multipurpose

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import io.bitcoinsv.spvchannels.host.R
import io.bitcoinsv.spvchannels.host.screens.binding.BindingFragment

abstract class MultiPurposeScreenFragment<Binding : ViewDataBinding,
    ViewModel : MultiPurposeScreenViewModel<*>> :
    BindingFragment<Binding, ViewModel>() {
    private lateinit var togglableViews: List<Int>
    private val ignoredViews = listOf(
        R.id.spinner,
        R.id.btn_perform,
        R.id.tv_response,
        R.id.tv_response_content,
    )

    protected abstract val layoutWrapper: ViewGroup
    protected abstract val button: Button
    protected abstract val spinner: Spinner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.visibility.observe { option ->
            togglableViews.forEach {
                view?.findViewById<View>(it)?.isVisible = option.views.contains(it)
            }
            button.setText(option.name)
            button.setOnClickListener { option.action() }
        }
    }

    private fun setupView() {
        togglableViews = layoutWrapper
            .children
            .map {
                it.id
            }.filter { !ignoredViews.contains(it) }
            .toList()

        setupSpinner()
    }

    private fun setupSpinner() {
        spinner.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.addAll(viewModel.options.map { getString(it.name) })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.selectItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}
