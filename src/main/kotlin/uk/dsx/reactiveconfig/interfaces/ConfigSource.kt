package uk.dsx.reactiveconfig.interfaces

import kotlinx.coroutines.channels.Channel
import uk.dsx.reactiveconfig.RawProperty

interface ConfigSource {
    suspend fun subscribe(dataStream: Channel<RawProperty>)
}