package me.y9san9.prizebot.database.participants_storage

import org.jetbrains.exposed.sql.Database


internal fun ParticipantsStorage(database: Database): ParticipantsStorage =
    TableParticipantsStorage(database)

internal interface ParticipantsStorage {
    fun saveParticipant(giveawayId: Long, userId: Long)
    fun getParticipantsIds(giveawayId: Long): List<Long>
    fun getParticipantsCount(giveawayId: Long): Int
    fun isParticipant(giveawayId: Long, userId: Long): Boolean
}
