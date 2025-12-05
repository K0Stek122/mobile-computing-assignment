package com.example.mcassignment.ui

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatCallback
import androidx.lifecycle.lifecycleScope
import com.example.mcassignment.data.db.AppDb
import com.example.mcassignment.data.db.entities.Questions
import com.example.mcassignment.databinding.ActivityGameBinding
import com.example.mcassignment.databinding.ActivityMenuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class GameActivity: AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding
    private val db by lazy { AppDb.getInstance(this) }
    private val dao by lazy { db.questionsDao() }

    private var questionCount: Int = 5
    private var difficulty: Int = -1

    private val currentQuestionIndex = 1

    private fun setInfoText() {
        val questionText = "Question ${currentQuestionIndex.toString()} of ${questionCount.toString()}"
        binding.questionInfoText.text = questionText
    }

    private suspend fun getRandomQuestions(count: Int): MutableList<Questions> {
        // By default List is non-mutable, thus we convert it to a Mutable List.
        val questionsList = withContext(Dispatchers.Main) {
            dao.getAllByDifficulty(difficulty)
        }.toMutableList()
        val selectedQuestions: MutableList<Questions> = mutableListOf() //Empty List

        for (i in 0..count) {
            val randomIndex = Random.nextInt(0, questionsList.size)
            selectedQuestions.add(questionsList.get(randomIndex))
            questionsList.removeAt(randomIndex)
        }
        return selectedQuestions
    }

    private fun displayNextQuestion(questionList: MutableList<Questions>) {
        val question = questionList.get(0)
        Toast.makeText(this, question.question, LENGTH_LONG).show()
        binding.questionText.setText(question.question)
        binding.answerARadio.setText(question.answer1)
        binding.answerBRadio.setText(question.answer2)
        binding.answerCRadio.setText(question.answer3)
        binding.answerDRadio.setText(question.answer4)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInfoText()

        questionCount = intent?.getIntExtra("QUESTION_COUNT", 5) ?: 5
        difficulty = intent?.getIntExtra("DIFFICULTY_ID", 2)?: 2
        lifecycleScope.launch {
            val questions: MutableList<Questions> = getRandomQuestions(questionCount)
            displayNextQuestion(questions)
        }
    }
}