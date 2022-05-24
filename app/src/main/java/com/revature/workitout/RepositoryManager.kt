package com.revature.workitout

import com.revature.workitout.model.room.repo.ExerciseRepo
import com.revature.workitout.model.room.repo.RoutineRepo

object RepositoryManager {
    lateinit var routineRepo: RoutineRepo
    lateinit var exerciseRepo:ExerciseRepo
}