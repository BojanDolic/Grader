package com.electrocoder.grader

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.electrocoder.Converters
import com.electrocoder.grader.entities.Predmet

@Database(entities = [Predmet::class], version = 3)
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