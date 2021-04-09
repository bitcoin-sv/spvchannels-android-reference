// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.options

import androidx.annotation.IdRes
import androidx.annotation.StringRes

data class Option(@StringRes val name: Int, @IdRes val views: List<Int>, val action: () -> Unit)
