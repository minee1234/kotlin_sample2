package com.minee.kotlin_sample2.part2chapter04

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minee.kotlin_sample2.part2chapter04.dao.HistoryDao
import com.minee.kotlin_sample2.part2chapter04.model.History

@Database(
    entities = [History::class],
    version = 1
)
abstract class P2C04Database : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}