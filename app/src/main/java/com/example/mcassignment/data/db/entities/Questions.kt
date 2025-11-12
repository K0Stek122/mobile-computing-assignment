package com.example.mcassignment.data.db.entities

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Questions(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val question: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val answerIndex: Int //A = 1, B = 2, C = 3, D = 4
)