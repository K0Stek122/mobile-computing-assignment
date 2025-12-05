package com.example.mcassignment.ui

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mcassignment.data.db.AppDb
import com.example.mcassignment.data.db.entities.Questions
import com.example.mcassignment.databinding.ActivityDesignBinding
import com.example.mcassignment.databinding.ActivityMenuBinding
import kotlinx.coroutines.launch

class DesignActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDesignBinding

    private val db by lazy { AppDb.getInstance(this) }
    private val dao by lazy { db.questionsDao() }

    private fun clearFields() {
        // Not a perfect solution. better to figure out a better way to do this later on.
        binding.questionName.setText("")

        binding.answerA.setText("")
        binding.answerB.setText("")
        binding.answerC.setText("")
        binding.answerD.setText("")

        binding.answerRadioGroup.clearCheck()
    }

    private fun estimateDifficulty(): Int {
        val questionLength = binding.questionName.text.toString().length
        if (questionLength < 10) {
            return 1 // Difficulty Easy
        }
        else if (questionLength < 25) {
            return 2 // Difficulty Medium
        }
        else {
            return 3 // Difficulty Hard
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDesignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitQuestion.setOnClickListener {
            lifecycleScope.launch {
                dao.insert(Questions(
                    question = binding.questionName.text.toString(),
                    answer1 = binding.answerA.text.toString(),
                    answer2 = binding.answerB.text.toString(),
                    answer3 = binding.answerC.text.toString(),
                    answer4 = binding.answerD.text.toString(),
                    correctAnswerIndex = binding.answerRadioGroup.checkedRadioButtonId.toInt(),
                    difficulty = estimateDifficulty()
                ))
                clearFields()
            }
        }
        binding.returnButton.setOnClickListener {
            val intent = Intent(this@DesignActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}