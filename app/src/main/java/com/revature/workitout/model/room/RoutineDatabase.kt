package com.revature.workitout.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.revature.workitout.model.room.entity.Routine
import com.revature.workitout.model.room.entity.RoutineComponent

@Database(
    entities = [Routine::class,RoutineComponent::class],
    version = 1,
    exportSchema = false
)
abstract class RoutineDatabase:RoomDatabase() {
    abstract fun routineDao(): RoutineDAO

    companion object{
        @Volatile
        private var INSTANCE: RoutineDatabase? = null
        fun getDataBase(context: Context):RoutineDatabase{
            val instance = INSTANCE
            if(instance != null)
                return instance

            synchronized(this){
                val tempInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        RoutineDatabase::class.java,
                        "RoutineDatabase"
                    ).fallbackToDestructiveMigration()
                        .build()
                INSTANCE = tempInstance
                return tempInstance
            }
        }

    }

}