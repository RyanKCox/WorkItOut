package com.revature.workitout.model.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.revature.workitout.model.room.entity.Routine

class RoutineRepo(app:Application) {

    private var routineDao: RoutineDAO

    init {
        val database = RoutineDatabase.getDataBase(app)
        routineDao = database.routineDao()
    }
    val getAllRoutines:LiveData<List<Routine>> = routineDao.fetchALlRoutines()

}