package com.example.entity

import com.example.database.table.Watchlists
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class WatchListEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<WatchListEntity>(Watchlists)

    var userId by UserEntity referencedOn Watchlists.userId
    var createdAt by Watchlists.createdAt
    var updatedAt by Watchlists.updatedAt
    var symbols by Watchlists.symbols
}
