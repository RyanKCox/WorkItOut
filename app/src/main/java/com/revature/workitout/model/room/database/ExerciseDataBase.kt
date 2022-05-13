package com.revature.workitout.model.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.revature.workitout.model.room.dao.ExerciseDAO
import com.revature.workitout.model.room.entity.ExerciseEntity

@Database(
    entities = [ExerciseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExerciseDataBase:RoomDatabase() {
    abstract fun exerciseDao():ExerciseDAO
    companion object{
        @Volatile
        private var INSTANCE :ExerciseDataBase? = null
        fun getDatabase(context:Context):ExerciseDataBase{
            val instance = INSTANCE
            if(instance != null)
                return instance

            synchronized(this){
                val tempInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        ExerciseDataBase::class.java,
                        "ExerciseDatabase"
                    ).fallbackToDestructiveMigration().build()
                INSTANCE = tempInstance
                return tempInstance
            }
        }
    }
}