package com.example.mcassignment.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mcassignment.data.db.entities.Questions

@Dao
interface QuestionsDao {
    @Insert suspend fun insert(question: Questions): Long
    @Query("SELECT * FROM questions ORDER BY id DESC") suspend fun getAll(): List<Questions>
    @Update suspend fun update(question: Questions): Int
    @Delete suspend fun delete(question: Questions): Int
}