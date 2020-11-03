package com.myapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user WHERE email = :mail LIMIT 1")
    suspend fun getUser(mail: String): User

    @Query("SELECT * FROM user WHERE loggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): User

    @Update
    suspend fun logout(user: User)
}