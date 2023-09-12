package com.example.database.table

import com.example.util.appconstants.GlobalConstants.PASSWORD_LENGTH
import com.example.util.appconstants.GlobalConstants.USER_NAME_LENGTH
import com.example.util.appconstants.UserConstants
import org.jetbrains.exposed.dao.id.UUIDTable

object Users : UUIDTable(UserConstants.USERS_TABLE_NAME) {
    val userName = varchar(UserConstants.USERS_USER_NAME, USER_NAME_LENGTH)
    val password = varchar(UserConstants.USERS_PASSWORD, PASSWORD_LENGTH)
}
