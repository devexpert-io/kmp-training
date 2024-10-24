package io.devexpert.kmptraining.data

import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable() {
    val googleId = varchar("google_id", 255).uniqueIndex()
    val email = varchar("email", 255)
    val name = varchar("name", 255)
    val pictureUrl = text("picture_url")
}