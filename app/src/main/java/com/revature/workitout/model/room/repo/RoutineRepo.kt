package com.revature.workitout.model.room.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.revature.workitout.model.room.database.RoutineDatabase
import com.revature.workitout.model.room.dao.RoutineDAO
import com.revature.workitout.model.room.entity.RoutineEntity

class RoutineRepo(app:Application) {

    private var routineDao: RoutineDAO

    init {
        val database = RoutineDatabase.getDataBase(app)
        routineDao = database.routineDao()
    }
    val getAllRoutines:List<RoutineEntity> = routineDao.fetchALlRoutines()

    fun addRoutine(routine:RoutineEntity){
        routineDao.insertRoutine(routine)
    }
    fun deleteRoutine(id:Int){
        val routine = routineDao.fetchRoutineById(id)
        routineDao.deleteRoutine(routine)
    }

}