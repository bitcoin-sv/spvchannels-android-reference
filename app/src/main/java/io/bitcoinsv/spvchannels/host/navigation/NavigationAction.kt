
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.navigation

import android.os.Bundle
import androidx.navigation.NavDirections

sealed class NavigationAction {
    object Pop : NavigationAction()
    class PopForResult(val requestKey: String, val bundle: Bundle) : NavigationAction()
    class NavigateTo(val navDirections: NavDirections) : NavigationAction()
}
