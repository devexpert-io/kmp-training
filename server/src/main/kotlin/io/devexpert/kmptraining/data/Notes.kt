package io.devexpert.kmptraining.data

import org.jetbrains.exposed.dao.id.IntIdTable

object Notes: IntIdTable() {
    val userId = varchar("user_id", 255)
    val title = varchar("title", 255)
    val content = text("content")
}