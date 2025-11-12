package com.example.mcassignment.ui

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDesignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitQuestion.setOnClickListener {
            lifecycleScope.launch {
                dao.insert(Questions(
                    question = binding.questionName.toString(),
                    answer1 = binding.answerA.toString(),
                    answer2 = binding.answerB.toString(),
                    answer3 = binding.answerC.toString(),
                    answer4 = binding.answerD.toString(),
                    correctAnswerIndex = binding.answerRadioGroup.checkedRadioButtonId.toInt()))

                clearFields()
            }
        }
    }
}