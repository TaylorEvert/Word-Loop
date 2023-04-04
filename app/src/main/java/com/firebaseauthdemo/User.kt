package com.firebaseauthdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false) val uid: Int,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "victories") val victories: Int = 0,
    @ColumnInfo(name = "defeats") val defeats: Int = 0,
    @ColumnInfo(name = "lastWord") val lastWord: String?
    )
