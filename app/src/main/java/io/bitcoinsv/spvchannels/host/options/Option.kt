package io.bitcoinsv.spvchannels.host.options

import androidx.annotation.IdRes
import androidx.annotation.StringRes

data class Option(@StringRes val name: Int, @IdRes val views: List<Int>, val action: () -> Unit)
