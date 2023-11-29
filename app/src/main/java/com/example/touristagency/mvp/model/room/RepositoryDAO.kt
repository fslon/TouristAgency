package com.example.touristagency.mvp.model.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: RoomGithubRepository)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: RoomGithubRepository)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<RoomGithubRepository>)

    @Update
    fun update(user: RoomGithubRepository)

    @Update
    fun update(vararg users: RoomGithubRepository)

    @Update
    fun update(users: List<RoomGithubRepository>)

    @Delete
    fun delete(user: RoomGithubRepository)

    @Delete
    fun delete(vararg users: RoomGithubRepository)

    @Delete
    fun delete(users: List<RoomGithubRepository>)

    @Query("SELECT * FROM RoomGithubRepository")
    fun getAll(): List<RoomGithubRepository>

    @Query("SELECT * FROM RoomGithubRepository WHERE userId = :userId")
    fun findForUser(userId: String): List<RoomGithubRepository>
}
