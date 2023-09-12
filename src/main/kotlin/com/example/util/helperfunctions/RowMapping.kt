package com.example.util.helperfunctions

import com.example.entity.UserEntity
import com.example.entity.WatchListEntity
import com.example.model.request.User
import com.example.model.response.Watchlist

fun toUserDetails(it: UserEntity): User {
    return User(it.id.value.toString(), it.userName, it.password)
}


fun toWatchlistEntity(it: WatchListEntity): Watchlist {
    return Watchlist(it.id.value.toString(), it.createdAt, it.updatedAt)
}
