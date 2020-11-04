package com.myapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "email") var email : String,
    @ColumnInfo(name = "password") var password : String,
    @ColumnInfo(name = "loggedIn") var loggedIn : Int = 0,
) : Serializable {
    @PrimaryKey( autoGenerate = true)
    var userId : Int = 0
}