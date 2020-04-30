package com.theapache64.tracktor.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index("username", "username", unique = true)]
)
data class UserEntity(
    @ColumnInfo(name = "username")
    val username: String, // theapache64

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String, // https://avatars1.githubusercontent.com/u/9678279?v=4

    @ColumnInfo(name = "type")
    val type: String, // User

    @ColumnInfo(name = "name")
    val name: String // theapache64
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "score")
    var score: Long = 0
}