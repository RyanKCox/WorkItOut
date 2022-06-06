package com.revature.workitout.model.repo

import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.entity.RoutineEntity
import com.revature.workitout.model.room.repo.IRoutineRepo

class fakeRoutineRepo: IRoutineRepo {

    val list = mutableListOf<Routine>()

    override fun addRoutine(routine: RoutineEntity): Long {
        val test = Routine(
            routineEntity = routine,
            exercises = mutableListOf()
        )
        list.add(test)
        return list.indexOf(test).toLong()
    }

    override fun deleteRoutine(routine: RoutineEntity) {
        var test:Routine? = null
        list.forEach {
            if(it.routineEntity.id == routine.id)
                test = it
        }
        if(test != null){
            list.remove(test)
        }
    }

    override suspend fun getAllRoutinesWithExercises(): List<Routine> {
        return list
    }

    override suspend fun addExerciseToRoutine(component: RoutineComponent) {
        TODO("Not yet implemented")
    }

    override fun insertComponent(component: RoutineComponent) {
        TODO("Not yet implemented")
    }

    override suspend fun getRoutineById(id: Long): Routine {
        TODO("Not yet implemented")
    }
}