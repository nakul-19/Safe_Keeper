package com.myapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    @ColumnInfo(name = "uid") var uid : Int,
    @ColumnInfo(name = "heading") var heading : String,
    @ColumnInfo(name = "body") var body : String
) : Serializable {
    @PrimaryKey( autoGenerate = true)
    var id : Int = 0
}