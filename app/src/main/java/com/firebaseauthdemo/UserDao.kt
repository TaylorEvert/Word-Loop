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
    suspend fun addUser(users: User)
    // Gets username from table

    @Query("SELECT username FROM user_table")
    fun getUser(): String


    /** */
    // Adds victory to the table
    @Query("UPDATE user_table SET victories = victories + 1")
    suspend fun addVictory()
    // Gets current victory count from table


    @Query("SELECT victories FROM user_table")
    fun getVictories(): Int


    /** */
    // Adds defeat to the table
    @Query("UPDATE user_table SET defeats = defeats + 1")
    suspend fun addDefeat()
    // Get current defeat count from table


    @Query("SELECT defeats FROM user_table")
    fun getDefeats(): Int

    /** */
    // Adds last word to the table
    //@Insert
    //suspend fun addWord(lastWord: String): User
    // Gets last word from table


    @Query("SELECT lastWord FROM user_table")
    fun getWord(): String


    // Delete last word from table
    //@Delete
    //suspend fun deleteWord(lastWord: String)


    @Delete
    suspend fun delete(user:User)
}