package com.example.mcassignment.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mcassignment.data.db.entities.Users

@Dao
interface UsersDao {
    @Insert suspend fun insert(user: Users): Long
    @Query("SELECT * FROM users ORDER BY id DESC") suspend fun getAll(): List<Users>
    @Update suspend fun update(user: Users): Int
    @Delete suspend fun delete(user: Users): Int

    @Query("SELECT COUNT(*) FROM users WHERE email = :email") suspend fun countByEmail(email: String): Int

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1") suspend fun findByEmail(email: String): Users?
}