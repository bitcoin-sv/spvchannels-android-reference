package com.nchain.spvchannels.host.util

/**
 * Used as a wrapper for data that is exposed via LiveData and should only be consumed once.
 * Recommended by AndroidDevelopers Google group.
 */
class Event<out T>(private val content: T) {
    var handled = false
        private set

    /**
     * Returns and consumes the content. Event is considered handled after calling this.
     */
    fun getContentIfNotHandled(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled
     */
    fun peek() = content
}
