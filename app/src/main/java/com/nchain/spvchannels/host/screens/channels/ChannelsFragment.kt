package com.nchain.spvchannels.host.screens.channels

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.databinding.FragmentChannelsBinding
import com.nchain.spvchannels.host.screens.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelsFragment : BindingFragment<FragmentChannelsBinding, ChannelsViewModel>() {
    override val layout = R.layout.fragment_channels
    override val viewModel by viewModels<ChannelsViewModel>()
    private lateinit var togglableViews: List<Int>
    private val ignoredViews = listOf(
        R.id.spinner,
        R.id.btn_perform,
        R.id.tv_response,
        R.id.tv_response_content,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        togglableViews = binding.layoutWrapper
            .children
            .map {
                it.id
            }.filter { !ignoredViews.contains(it) }
            .toList()

        setupSpinner()
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.visibility.observe { option ->
            togglableViews.forEach {
                view?.findViewById<View>(it)?.isVisible = option.views.contains(it)
            }
            binding.btnPerform.setText(option.name)
            binding.btnPerform.setOnClickListener { option.action() }
        }
    }

    private fun setupSpinner() {
        binding.spinner.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.addAll(viewModel.options.map { getString(it.name) })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
