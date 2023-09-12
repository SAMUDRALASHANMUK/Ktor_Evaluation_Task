package com.example.di

import com.example.dao.RecentWatchlistDAO
import com.example.dao.UserDAO
import com.example.dao.WatchlistDAO
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.bind
import com.example.services.RecentWatchlistService
import com.example.services.WatchlistService
import com.example.services.UserService
import com.example.repository.RecentWatchlistDAOImpl
import com.example.repository.UserDAOImpl
import com.example.repository.WatchlistDAOImpl

val appModule = module {
    singleOf(::WatchlistDAOImpl) { bind<WatchlistDAO>() }
    singleOf(::RecentWatchlistDAOImpl) { bind<RecentWatchlistDAO>() }
    singleOf(::UserDAOImpl) { bind<UserDAO>() }
    singleOf(::WatchlistService)
    singleOf(::UserService)
    singleOf(::RecentWatchlistService)
}
