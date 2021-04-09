// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.channels

import io.bitcoinsv.spvchannels.channels.models.ChannelInfo
import io.bitcoinsv.spvchannels.channels.models.ChannelPermissions
import io.bitcoinsv.spvchannels.channels.models.Retention
import io.bitcoinsv.spvchannels.channels.models.TokenInfo
import io.bitcoinsv.spvchannels.channels.models.api.CreateRequest
import io.bitcoinsv.spvchannels.channels.models.api.CreateTokenRequest
import io.bitcoinsv.spvchannels.response.Status
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class Channel internal constructor(
    private val service: ChannelService,
    private val accountId: String,
    private val context: CoroutineContext,
) {
    /**
     * Returns a list of all Channels
     */
    suspend fun getAllChannels(): Status<List<ChannelInfo>> = withContext(context) {
        Status.fromResponse(
            service.getAllChannels(accountId)
        ) {
            it.channels
        }
    }

    /**
     * Creates a new Channel, owned by the account holder.
     *
     * @param publicRead whether the channel is readable
     * @param publicWrite whether the channel can be written to
     * @param sequenced whether the channel should be sequenced
     * @param retention the settings for channel messages retention
     */
    suspend fun createChannel(
        publicRead: Boolean,
        publicWrite: Boolean,
        sequenced: Boolean,
        retention: Retention,
    ): Status<ChannelInfo> = withContext(context) {
        Status.fromResponse(
            service.createChannel(
                accountId,
                CreateRequest(publicRead, publicWrite, sequenced, retention)
            )
        )
    }

    /**
     * Updates Channel metadata and permissions (read/write and locking a channel)
     *
     * Locked channel definition:
     *  - writing to a locked channel is not allowed
     *  - reading from a locked channel is allowed
     *  - restrictions are implemented on server side
     *
     * @param channelId the channel to amend
     * @param publicRead whether the channel is readable
     * @param publicWrite whether the channel can be written to
     * @param locked whether or not the channel is locked
     */
    suspend fun amendChannel(
        channelId: String,
        publicRead: Boolean,
        publicWrite: Boolean,
        locked: Boolean,
    ): Status<ChannelPermissions> = withContext(context) {
        Status.fromResponse(
            service.amendChannel(
                accountId,
                channelId,
                ChannelPermissions(publicRead, publicWrite, locked)
            )
        )
    }

    /**
     * Returns the information specific to the channel.
     *
     * @param channelId the channel to retrieve the information for
     */
    suspend fun getChannel(channelId: String): Status<ChannelInfo> = withContext(context) {
        Status.fromResponse(
            service.getChannel(accountId, channelId)
        )
    }

    /**
     * Deletes the specified channel.
     *
     * @param channelId the channel to delete
     */
    suspend fun deleteChannel(channelId: String): Status<Unit> = withContext(context) {
        Status.fromResponse(
            service.deleteChannel(accountId, channelId)
        )
    }

    /**
     * Returns a list of channel tokens. Can be made to filter on a specific token value.
     *
     * @param channelId the channel to return the tokens for
     * @param token optional token parameter to filter for
     */
    suspend fun getChannelTokens(
        channelId: String,
        token: String? = null
    ): Status<List<TokenInfo>> = withContext(context) {
        Status.fromResponse(
            service.getTokens(accountId, channelId, token)
        )
    }

    /**
     * Returns the information for a single token.
     *
     * @param channelId the channel where the token exists
     * @param token the token to retrieve the information for
     */
    suspend fun getTokenInfo(
        channelId: String,
        token: String,
    ): Status<TokenInfo> = withContext(context) {
        Status.fromResponse(
            service.getTokenInfo(accountId, channelId, token)
        )
    }

    /**
     * Generate a new API token for the given channel.
     *
     * @param channelId the channel for which to generate a token for
     * @param description the description for the token
     * @param canRead whether or not the token has the read permission
     * @param canWrite whether or not the token has the write permission
     */
    suspend fun createToken(
        channelId: String,
        description: String,
        canRead: Boolean,
        canWrite: Boolean,
    ): Status<TokenInfo> = withContext(context) {
        Status.fromResponse(
            service.createToken(
                accountId,
                channelId,
                CreateTokenRequest(description, canRead, canWrite)
            )
        )
    }

    /**
     * Used to revoke the specific token for the given channel.
     *
     * @param channelId the channel on which to revoke the token
     * @param token the token to revoke
     */
    suspend fun revokeToken(
        channelId: String,
        token: String,
    ): Status<Unit> = withContext(context) {
        Status.fromResponse(
            service.revokeToken(
                accountId,
                channelId,
                token
            )
        )
    }
}
