package com.minee.kotlin_sample2.part3chapter04

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.minee.kotlin_sample2.part3chapter04.dao.HistoryDao
import com.minee.kotlin_sample2.part3chapter04.dao.ReviewDao
import com.minee.kotlin_sample2.part3chapter04.model.History
import com.minee.kotlin_sample2.part3chapter04.model.Review

@Database(entities = [History::class, Review::class], version = 2)
//@Database(entities = [History::class], version = 1)
abstract class P3C04Database : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}

fun getDatabase(context: Context): P3C04Database {

    val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE 'REVIEW' ('id' INTEGER, 'review' TEXT, PRIMARY KEY('id'))")
        }
    }

    return Room.databaseBuilder(
        context,
        P3C04Database::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration_1_2)
        .build()
}