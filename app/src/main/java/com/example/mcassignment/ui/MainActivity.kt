package com.example.mcassignment.ui

import android.content.Intent
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

/*
All activities in this project will be built with ConstraintLayouts.
It is a method of responsive UI design that lets you create large complex layouts with a flat hierarchy.
It does not require nested views making it very simple to build.
One disadvantage of this is that as the app grows and the number of widgets increases, all the relationships between different widgets will make the app very complex and
difficult to maintain and edit the layout.

https://developer.android.com/develop/ui/views/layout/constraint-layout
 */

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

    private suspend fun loginUser(email: String, password: String): Boolean {
        if (userExists(email) == 0) {
            setNotificationText("This email does not exist.")
            return false
        }
        lifecycleScope.launch {
            try {
                val users = dao.getAll()
                for (user in users) {
                    if (user.email == email && user.password == password) {
                        val intent = Intent(this@MainActivity, MenuActivity::class.java)
                        intent.putExtra("USER_ID", user.id.toInt()) // Pass the user ID to next activity
                        startActivity(intent)
                    }
                }
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
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray()) // Hashes the password
        return hash.fold("") { str, bytes -> str + "%02x".format(bytes) } // Salts the password to prevent Rainbow Table Attacks.
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

        /*
        An alternative to Bindings is FindViewByID. An older, now deprecated, function that gets a specific XML bindings.
        This has been replaced by bindings that make it easier to retrieve all IDs from an activity.xml.

        Bindings are automatically generated based on the naming conventions. For example:
        MainActivity.kt automatically maps to activity_main.xml
        TestActivity.kt automatically maps to activity_test.xml
        FoobarActivity.kt automatically maps to activity_foobar.xml
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set the view to activity_main.xml

        // REGISTER BUTTON PRESS
        binding.registerButton.setOnClickListener {
            if (inputBoxesEmpty()) {
                setNotificationText("Email or Password invalid, please enter the data.")
                return@setOnClickListener //the return@label statement ensures the compiler know which function to exit from. e.g. This will NOT exit onCreate, only setOnClickListener.
            }

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
                } catch (e: Exception) {
                    setNotificationText(e.toString())
                }
            }
        }

        // LOGIN USER
        binding.loginButton.setOnClickListener {
            if (inputBoxesEmpty()) {
                setNotificationText("Email or Password invalid, please enter the data.")
                return@setOnClickListener
            }
            lifecycleScope.launch {
                loginUser(binding.emailInput.text.toString(), binding.passwordInput.text.toString().toString())
            }
        }
    }
}