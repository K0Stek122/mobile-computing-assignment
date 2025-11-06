package com.example.mcassignment.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mcassignment.data.db.entities.Users

// This builds the database.
@Database(entities = [Users::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun usersDao() : UsersDao // Initialise the Users entity via Data Access Object (DAO)

    companion object {
        @Volatile private var INSTANCE: AppDb? = null // FIrstly initialise the database as mutable NULL object.
        fun getInstance(context: Context): AppDb = // Create the Database
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "app.db" // Name the database "app.db"
                ).fallbackToDestructiveMigration(false).build().also { INSTANCE = it } //set variable INSTANCE to the output of getInstance()
                //fallbackToDestructiveMigration is there for debug purposes. It deletes and remakes the database every run.
            }
    }
}