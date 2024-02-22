package io.devexpert.eventapp.data

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Talks : IntIdTable() {
    val title = varchar("title", 255)
    val description = varchar("description", 2000)
    val time = varchar("time", 50)
    val speaker = reference("speaker", Speakers)
}

object Speakers : IntIdTable() {
    val name = varchar("name", 50)
    val bio = varchar("bio", 2000)
}

object SpeakerSocialLinks : Table() {
    val speaker = reference("speaker", Speakers)
    val socialNetwork = varchar("socialNetwork", 50)
    val link = varchar("link", 255)
}