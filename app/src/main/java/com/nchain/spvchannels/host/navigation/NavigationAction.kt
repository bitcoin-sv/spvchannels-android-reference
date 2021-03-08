package com.nchain.spvchannels.host.navigation

import android.os.Bundle
import androidx.navigation.NavDirections

sealed class NavigationAction {
    object Pop : NavigationAction()
    class PopForResult(val requestKey: String, val bundle: Bundle) : NavigationAction()
    class NavigateTo(val navDirections: NavDirections) : NavigationAction()
}
