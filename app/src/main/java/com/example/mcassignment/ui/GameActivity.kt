package com.example.mcassignment.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatCallback
import com.example.mcassignment.data.db.AppDb
import com.example.mcassignment.data.db.entities.Questions
import com.example.mcassignment.databinding.ActivityGameBinding
import com.example.mcassignment.databinding.ActivityMenuBinding
import kotlin.random.Random

class GameActivity: AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding
    private val db by lazy { AppDb.getInstance(this) }
    private val dao by lazy { db.questionsDao() }

    private val questionCount = intent.getIntExtra("QUESTION_COUNT", 5)

    private val currentQuestionIndex = 1

    private fun setInfoText() {
        val questionText = "Question ${currentQuestionIndex.toString()} of ${questionCount.toString()}"
        binding.questionInfoText.text = questionText
    }

    private suspend fun getRandomQuestions(count: Int): List<Questions> {
        // By default List is non-mutable, thus we convert it to a Mutable List.
        val questionsList = dao.getAll().toMutableList()
        var selectedQuestions: MutableList<Questions> = mutableListOf() //Empty List

        for (i in 0..count) {
            val randomIndex = Random.nextInt(0, questionsList.size)
            selectedQuestions.add(questionsList.get(randomIndex))
            questionsList.removeAt(randomIndex)
        }
        return selectedQuestions.toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInfoText()
    }
}