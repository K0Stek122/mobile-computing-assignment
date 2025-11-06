package com.example.mcassignment.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mcassignment.data.db.AppDb
import com.example.mcassignment.data.db.entities.Users
import com.example.mcassignment.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val db by lazy { AppDb.getInstance(this) }
    private val dao by lazy { db.usersDao() }

    // Notification text is red text below the "Register Button" to inform the user of any errors or successes.
    private fun setNotificationText(text: String) {
        binding.loginNotification.text = text
    }

    private suspend fun registerUser(email: String, password: String): Boolean {
        if (userExists(email) == 1) {
            setNotificationText("User under this email already exists. Please try a different email or login.")
            return false
        }
        lifecycleScope.launch {
            try {
                dao.insert(Users(email = email, password = password))
            } catch (e: Exception) {
                setNotificationText(e.toString())
            }
        }
        return true
    }

    private fun inputBoxesEmpty(): Boolean {
        if (binding.emailInput.text.isEmpty() || binding.passwordInput.text.isEmpty()) {
            return true
        }
        else {
            return false
        }
    }

    // 1 for user exists
    // 0 for user not exists
    // -1 for empty user list.
    // Complexity is O(n) It's not good but enough for this project.
    // TODO Implement Binary Search
    private suspend fun userExists(email: String): Int {
        //withContext ensures asynchronous action. The function will wait until users will finish fetching.
        val users: List<Users> = withContext(Dispatchers.IO) {
            dao.getAll()
        }

        if (users.isEmpty()) {
            return -1
        }
        for (user in users) {
            if (user.email == email) {
                return 1
            }
        }
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set the view to activity_main.xml

        // REGISTER BUTTON PRESS
        binding.registerButton.setOnClickListener {
            if (inputBoxesEmpty()) {
                setNotificationText("Email or Password invalid, please enter the data.")
                return@setOnClickListener //the return@label statement ensures the compiler know which function to exit from. e.g. This will NOT exit onCreate, only setOnClickListener.
            }

            // UserExists
            lifecycleScope.launch {
                try {
                    if (userExists(binding.emailInput.text.toString()) == 1) {
                        setNotificationText("User under this email already exists.")
                        return@launch
                    }
                    registerUser(binding.emailInput.text.toString(), binding.passwordInput.text.toString())
                    binding.emailInput.setText("")
                    binding.passwordInput.setText("")

                    setNotificationText("User registered successfully.")
                } catch(e: Exception) {
                    setNotificationText(e.toString())
                }
            }
        }
    }
}