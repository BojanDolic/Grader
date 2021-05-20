package com.electrocoder.grader.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.electrocoder.Converters
import com.electrocoder.grader.PredmetDAO
import com.electrocoder.grader.entities.Ocjena
import com.electrocoder.grader.entities.Predmet

@Database(entities = [Predmet::class, Ocjena::class], version = 10)
@TypeConverters(Converters::class)
abstract class PredmetiDatabase : RoomDatabase() {

    abstract fun predmetDAO(): PredmetDAO

    companion object {
        @Volatile
        private var instance: PredmetiDatabase? = null

        fun getInstance(context: Context): PredmetiDatabase? {

            if(instance == null) {
                synchronized(PredmetiDatabase::class.java) {
                    if(instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            PredmetiDatabase::class.java,
                            "baza_predmeta"
                        ).fallbackToDestructiveMigration().build()
                    }
                }

            }
            return instance
        }

    }


}