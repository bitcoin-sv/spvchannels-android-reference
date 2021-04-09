
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import io.bitcoinsv.spvchannels.host.BR
import io.bitcoinsv.spvchannels.host.navigation.NavigationAction
import io.bitcoinsv.spvchannels.host.util.Event

abstract class BindingFragment<Binding : ViewDataBinding, ViewModel : CommonViewModel> :
    Fragment() {
    protected lateinit var binding: Binding

    @get:LayoutRes
    protected abstract val layout: Int
    protected abstract val viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        setupViewModel()
        return binding.root
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(
            args?.apply {
                putBundle(BUNDLE_ARGS, Bundle(this))
            }
        )
    }

    @CallSuper
    protected open fun setupViewModel() {
        viewModel.navDirections.observeEvent {
            val navController = findNavController()
            when (it) {
                is NavigationAction.Pop -> navController.popBackStack()
                is NavigationAction.PopForResult -> {
                    setFragmentResult(it.requestKey, it.bundle)
                    navController.popBackStack()
                }
                is NavigationAction.NavigateTo -> navController.navigate(it.navDirections)
            }
        }
        binding.setVariable(BR.viewModel, viewModel)
    }

    fun <T> LiveData<T>.observe(observer: (value: T) -> Unit) {
        observe(viewLifecycleOwner) {
            it?.let(observer)
        }
    }

    fun <T> LiveData<Event<T>>.observeEvent(observer: (value: T) -> Unit) {
        observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let(observer)
        }
    }

    companion object {
        const val BUNDLE_ARGS = "BUNDLE_ARGS"
    }
}
