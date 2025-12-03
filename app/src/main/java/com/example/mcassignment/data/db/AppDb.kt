package com.example.mcassignment.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mcassignment.data.db.entities.Users
import com.example.mcassignment.data.db.entities.Questions

/* This builds the database.
Kotlin Room framework is used to build the database.
Room is a complex wrapper around SQLite that utilises data-driven practices.
Every Room database is composed of three elements:
- Database
- Entities
- DAOs

DAOs are tiny interfaces that connect to the database to access entities. It acts as a translation layer between the database and the tables in the database.
DAOs contain wrappers around common SQL functions, like "SELECT *", "INSERT", etc...
*/
@Database(entities = [Users::class, Questions::class], version = 2)
abstract class AppDb : RoomDatabase() {
    abstract fun usersDao() : UsersDao // Initialise the Users entity via Data Access Object (DAO)
    abstract fun questionsDao(): QuestionsDao

    companion object {
        @Volatile private var INSTANCE: AppDb? = null // Firstly initialise the database as mutable NULL object.
        fun getInstance(context: Context): AppDb = // Create the Database
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "app.db" // Name the database "app.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .allowMainThreadQueries()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val c = db.query("SELECT name FROM sqlite_master WHERE type='table'")
                            while (c.moveToNext()) {
                                android.util.Log.d("ROOM", "Table: ${c.getString(0)}")
                            }
                            c.close()
                        }
                    })
                    .build().also { INSTANCE = it }
            }
    }
}