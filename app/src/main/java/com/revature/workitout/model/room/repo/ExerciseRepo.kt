package com.revature.workitout.model.room.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.revature.workitout.model.retrofit.responses.Exercise
import com.revature.workitout.model.room.dao.ExerciseDAO
import com.revature.workitout.model.room.database.ExerciseDataBase
import com.revature.workitout.model.room.entity.ExerciseEntity

class ExerciseRepo(app:Application) {

    private var exerciseDao:ExerciseDAO
    init {
        val instance = ExerciseDataBase.getDatabase(app)
        exerciseDao = instance.exerciseDao()
    }

    val getAllExercises:List<ExerciseEntity> =
            exerciseDao.fetchAllExercises()

    fun getExerciseById(id:Int):ExerciseEntity{
        return exerciseDao.fetchExerciseById(id)
    }

    fun getExercisesByBodypart(bodypart:String):List<ExerciseEntity>{
        return exerciseDao.fetchExerciseByBodypart(bodypart)
    }

    fun getAllBodyParts():List<String>{
        return exerciseDao.fetchAllBodyParts()
    }

    fun insertExercise(exercise:ExerciseEntity):Long{
        return exerciseDao.insertExercise(exercise)
    }

    fun deleteExercise(exercise: ExerciseEntity){
        exerciseDao.deleteExercise(exercise)
    }
}