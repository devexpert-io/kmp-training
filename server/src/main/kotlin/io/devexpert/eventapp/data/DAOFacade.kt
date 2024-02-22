package io.devexpert.eventapp.data

import io.devexpert.eventapp.domain.SocialNetwork
import io.devexpert.eventapp.domain.Speaker
import io.devexpert.eventapp.domain.Talk
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface DAOFacade {
    suspend fun getAllTalks(): List<Talk>
    suspend fun createTalk(talk: Talk)
    suspend fun updateTalk(talk: Talk): Boolean
    suspend fun deleteTalk(id: Int): Boolean
}

val dao = DAOFacadeImpl()

class DAOFacadeImpl : DAOFacade {
    override suspend fun getAllTalks(): List<Talk> = dbQuery {
        (Talks innerJoin Speakers).selectAll().map {
            Talk(
                id = it[Talks.id].value,
                title = it[Talks.title],
                description = it[Talks.description],
                time = it[Talks.time],
                speaker = Speaker(
                    id = it[Speakers.id].value,
                    name = it[Speakers.name],
                    bio = it[Speakers.bio],
                    socialLinks = findNetworksBySpeaker(it[Speakers.id].value)
                )
            )
        }
    }

    private fun findNetworksBySpeaker(speakerId: Int): Map<SocialNetwork, String> {
        return SpeakerSocialLinks
            .selectAll().where { SpeakerSocialLinks.speaker eq speakerId }
            .associate { result ->
                SocialNetwork.valueOf(result[SpeakerSocialLinks.socialNetwork]) to result[SpeakerSocialLinks.link]
            }
    }

    override suspend fun createTalk(talk: Talk) = dbQuery {
        val speakerId = Speakers
            .selectAll()
            .where { Speakers.name eq talk.speaker.name }
            .singleOrNull()
            ?.get(Speakers.id)?.value
            ?: Speakers.insertAndGetId {
                it[name] = talk.speaker.name
                it[bio] = talk.speaker.bio
            }.value

        Talks.insert {
            it[title] = talk.title
            it[description] = talk.description
            it[time] = talk.time
            it[speaker] = speakerId
        }

        talk.speaker.socialLinks.forEach { (network, link) ->
            SpeakerSocialLinks.insert {
                it[this.speaker] = speakerId
                it[this.socialNetwork] = network.name
                it[this.link] = link
            }
        }
    }

    override suspend fun updateTalk(talk: Talk): Boolean = dbQuery {
        val speakerId = Speakers.update({ Speakers.id eq talk.speaker.id }) {
            it[name] = talk.speaker.name
            it[bio] = talk.speaker.bio
        }

        val updatedTasks = Talks.update({ Talks.id eq talk.id }) {
            it[title] = talk.title
            it[description] = talk.description
            it[time] = talk.time
            it[speaker] = speakerId
        }

        talk.speaker.socialLinks.forEach { (network, link) ->
            SpeakerSocialLinks.update({ SpeakerSocialLinks.speaker eq speakerId and (SpeakerSocialLinks.socialNetwork eq network.name) }) {
                it[this.link] = link
            }
        }

        updatedTasks > 0
    }

    override suspend fun deleteTalk(id: Int): Boolean = dbQuery {
        val talk = Talks.selectAll().where { Talks.id eq id }.singleOrNull() ?: return@dbQuery false

        SpeakerSocialLinks.deleteWhere { speaker eq talk[Talks.speaker] }

        val deletedTalks = Talks.deleteWhere { Talks.id eq id }

        val speakerHasNoMoreTalks = Talks.selectAll().where { Talks.speaker eq talk[Talks.speaker] }
            .empty()

        if (speakerHasNoMoreTalks) {
            Speakers.deleteWhere { Speakers.id eq talk[Talks.speaker] }
        }

        deletedTalks > 0
    }
}