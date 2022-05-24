package com.revature.workitout.model.room.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.database.RoutineDatabase
import com.revature.workitout.model.room.dao.RoutineDAO
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.entity.RoutineEntity

class RoutineRepo(app:Application) {

    private var routineDao: RoutineDAO

    init {
        val database = RoutineDatabase.getDataBase(app)
        routineDao = database.routineDao()
    }
    val getAllRoutines:LiveData<List<RoutineEntity>> = routineDao.fetchALlRoutines()

    fun getRoutineByID(id:Long):RoutineEntity{
        return routineDao.fetchRoutineById(id)
    }

    fun getExercisesOfRoutine(id:Int): List<RoutineComponent>{
        return routineDao.fetchRoutineComponentsById(id)
    }
    fun addRoutine(routine:RoutineEntity):Long{
        return routineDao.insertRoutine(routine)
    }
    fun deleteRoutineById(id:Int){
        val routine = routineDao.fetchRoutineById(id.toLong())
        routineDao.deleteRoutine(routine)
    }
    fun deleteRoutine(routine:RoutineEntity){
        routineDao.deleteRoutineComponentsByRoutineID(routine.id.toLong())
        routineDao.deleteRoutine(routine)
    }
    suspend fun getAllRoutinesWithExercises():List<Routine>{
        return routineDao.getRoutinesWithExercises()
    }

}