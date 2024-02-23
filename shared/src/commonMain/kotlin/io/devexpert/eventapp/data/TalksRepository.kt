package io.devexpert.eventapp.data

import io.devexpert.eventapp.domain.Talk
import io.devexpert.eventapp.serverUrl
import io.ktor.client.call.body
import io.ktor.client.request.get

class TalksRepository {
    suspend fun getTalks(): List<Talk> = RemoteClient.instance.get("$serverUrl/talks").body()
}