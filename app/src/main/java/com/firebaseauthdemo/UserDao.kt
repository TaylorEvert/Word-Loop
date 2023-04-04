package com.firebaseauthdemo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// Define methods to access the db - Data Access Objects

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user_table")
    fun getAll(): List<User>

    @Query("SELECT * FROM user_table WHERE username LIKE :username LIMIT 1")
    suspend fun findByName(username: String): User

    @Insert
    suspend fun insertAll(vararg users: User)


    /** */
    // Adds username to the table
    @Insert
    suspend fun addUser(username: String): User
    // Gets username from table
    @Query("SELECT username FROM user_table")
    suspend fun getUser(username: String): User


    /** */
    // Adds victory to the table
    @Insert
    suspend fun addVictory(victories: Int): User
    // Gets current victory count from table
    @Query("SELECT victories FROM user_table")
    suspend fun getVictories(victories: Int): User


    /** */
    // Adds defeat to the table
    @Insert
    suspend fun addDefeat(defeats: Int): User
    // Get current defeat count from table
    @Query("SELECT defeats FROM user_table")
    suspend fun getDefeats(defeats: Int): User

    /** */
    // Adds last word to the table
    @Insert
    suspend fun addWord(lastWord: String): User
    // Gets last word from table
    @Query("SELECT lastWord FROM user_table")
    suspend fun getWord(lastWord: String): User
    // Delete last word from table
    @Delete
    suspend fun deleteWord(lastWord: String)


    @Delete
    suspend fun delete(user:User)
}