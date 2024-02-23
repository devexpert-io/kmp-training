package io.devexpert.eventapp.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.devexpert.eventapp.sampleTalks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {

    private fun createHikariDataSource() = HikariDataSource(HikariConfig().apply {
        driverClassName = "org.h2.Driver"
        jdbcUrl = "jdbc:h2:file:./build/db"
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    fun init() {
        val database = Database.connect(createHikariDataSource())

        transaction(database) {
            SchemaUtils.create(Talks, Speakers, SpeakerSocialLinks)
            runBlocking {
                if (Talks.selectAll().empty()) {
                    sampleTalks.forEach {
                        dao.createTalk(it)
                    }
                }
            }
        }
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }