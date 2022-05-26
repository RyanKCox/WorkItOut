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
    fun addExerciseToRoutine(component: RoutineComponent)
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
    override fun addExerciseToRoutine(component: RoutineComponent){
        routineDao.insertComponent(component)
    }

}