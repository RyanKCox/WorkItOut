package com.revature.workitout.model.room.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.database.RoutineDatabase
import com.revature.workitout.model.room.dao.RoutineDAO
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.entity.RoutineEntity

interface IRoutineRepo {
    fun addRoutine(routine: RoutineEntity): Long
    fun deleteRoutine(routine: RoutineEntity)
    suspend fun getAllRoutinesWithExercises(): List<Routine>
    suspend fun addExerciseToRoutine(component: RoutineComponent)
    fun insertComponent(component: RoutineComponent)
    suspend fun getRoutineById(id:Long):Routine
}

class RoutineRepo(app:Application) : IRoutineRepo {

    private var routineDao: RoutineDAO

    init {
        val database = RoutineDatabase.getDataBase(app)
        routineDao = database.routineDao()
    }
    override fun addRoutine(routine:RoutineEntity):Long{
        return routineDao.insertRoutine(routine)
    }
    override fun deleteRoutine(routine:RoutineEntity){
        routineDao.deleteRoutineComponentsByRoutineID(routine.id)
        routineDao.deleteRoutine(routine)
    }
    override suspend fun getAllRoutinesWithExercises():List<Routine>{
        return routineDao.getRoutinesWithExercises()
    }
    override suspend fun addExerciseToRoutine(component: RoutineComponent){
        routineDao.insertComponent(component)
    }

    override fun insertComponent(component: RoutineComponent) {
        routineDao.insertComponent(component)
    }

    override suspend fun getRoutineById(id: Long):Routine {
        return routineDao.getRoutineFromID(id)
    }

}