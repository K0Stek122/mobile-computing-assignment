package com.example.mcassignment.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mcassignment.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMenuBinding

    private var userId: Long = -1

    private fun questionCountCorrect(): Boolean {
        if (binding.questionCount.text.toString() == "" || binding.questionCount.text.toString().toInt() >= 20) {
            binding.notificationText.text = "Question count incorrect. Question count has to be less than or equal to 20"
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent?.getLongExtra("USER_ID", -1L) ?: -1L
        /* TODO pass user id to DesignActivity.
        if (userId == -1L) {
            Toast.makeText(this, "Missing user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
         */

        // START QUIZ
        // TODO Error if not enough questions in the pool.
        binding.startQuizButton.setOnClickListener {
            if (!questionCountCorrect()) {
                return@setOnClickListener
            }
            val intent = Intent(this@MenuActivity, GameActivity::class.java)
            intent.putExtra("QUESTION_COUNT", binding.questionCount.text.toString().toInt())
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        // DESIGN QUESTIONS
        binding.designButton.setOnClickListener {
            val intent = Intent(this@MenuActivity, DesignActivity::class.java)
            startActivity(intent)

        }
    }
}