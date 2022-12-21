package com.example.googlebooks.data.local.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.googlebooks.data.local.dao.StarDao
import com.example.googlebooks.data.local.entity.StarEntity

@Database(
    entities = [StarEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun starDao(): StarDao
}