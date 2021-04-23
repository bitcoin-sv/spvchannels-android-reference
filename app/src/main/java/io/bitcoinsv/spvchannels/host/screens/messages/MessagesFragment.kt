// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.messages

import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.bitcoinsv.spvchannels.host.R
import io.bitcoinsv.spvchannels.host.databinding.FragmentMessagesBinding
import io.bitcoinsv.spvchannels.host.screens.multipurpose.MultiPurposeScreenFragment
import io.bitcoinsv.spvchannels.notifications.Notification

@AndroidEntryPoint
class MessagesFragment : MultiPurposeScreenFragment<FragmentMessagesBinding, MessagesViewModel>() {
    override val layout = R.layout.fragment_messages
    override val viewModel by viewModels<MessagesViewModel>()
    override val layoutWrapper by lazy { binding.layoutWrapper }
    override val button by lazy { binding.btnPerform }
    override val spinner by lazy { binding.spinner }

    override fun setupViewModel() {
        super.setupViewModel()

        viewModel.notification.observe { event ->
            event.getContentIfNotHandled()?.let {
                showNotification(it)
            }
        }
    }

    private fun showNotification(notification: Notification) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Received notification")
            .setMessage(
                """Received message:
                |${notification.message}
                |for date:
                |${notification.time}""".trimMargin()
            )
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
