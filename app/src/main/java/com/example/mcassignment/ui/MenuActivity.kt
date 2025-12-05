package com.example.mcassignment.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mcassignment.data.db.AppDb
import com.example.mcassignment.databinding.ActivityMenuBinding
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMenuBinding

    private var userId: Long = -1

    private val db by lazy { AppDb.getInstance(this) }
    private val dao by lazy { db.questionsDao() }

    private fun questionCountCorrect(): Boolean {
        if (binding.questionCount.text.toString() == "" || binding.questionCount.text.toString().toInt() >= 20) {
            binding.notificationText.text = "Question count incorrect. Question count has to be less than or equal to 20"
            return false
        }
        return true
    }

    private fun isQuestionPoolPopulated(questionCount: Int): Boolean {
        var questionPoolCount: Int? = null
        lifecycleScope.launch {
            questionPoolCount = dao.getAll().count()
        }
        if (questionPoolCount != null) {
            if (questionPoolCount >= questionCount) {
                return true
            }
            else {
                return false
            }
        }
        else {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent?.getLongExtra("USER_ID", -1L) ?: -1L
        if (userId == -1L) {
            Toast.makeText(this, "Missing user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // START QUIZ
        binding.startQuizButton.setOnClickListener {
            if (!questionCountCorrect()) {
                return@setOnClickListener
            }
            if (isQuestionPoolPopulated(questionCount = binding.questionCount.text.toString().toInt())) {
                Toast.makeText(this, "Question pool doesn't have enough questions!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(this@MenuActivity, GameActivity::class.java)
            intent.putExtra("QUESTION_COUNT", binding.questionCount.text.toString().toInt())
            intent.putExtra("USER_ID", userId)
            intent.putExtra("DIFFICULTY_ID", binding.difficultyRadioGroup.checkedRadioButtonId)
            startActivity(intent)
        }

        // DESIGN QUESTIONS
        binding.designButton.setOnClickListener {
            val intent = Intent(this@MenuActivity, DesignActivity::class.java)
            startActivity(intent)

        }
    }
}