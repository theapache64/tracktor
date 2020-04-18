package com.theapache64.tracktor.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sample")
data class SampleEntity(
    @ColumnInfo(name = "data") val userName: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}