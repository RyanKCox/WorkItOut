package com.revature.workitout.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.revature.workitout.model.room.entity.ExerciseEntity

@Dao
interface ExerciseDAO {

    @Query("SELECT * FROM ExerciseList")
    fun fetchAllExercises():List<ExerciseEntity>

    @Query("SELECT * FROM ExerciseList WHERE id = :id")
    fun fetchExerciseById(id:Int):ExerciseEntity

    @Query("SELECT * FROM ExerciseList WHERE sBodypart LIKE :bodypart")
    fun fetchExerciseByBodypart(bodypart:String):List<ExerciseEntity>


    @Query("SELECT DISTINCT sBodypart FROM ExerciseList ORDER BY sBodypart")
    fun fetchAllBodyParts():List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(exercise:ExerciseEntity):Long

    @Delete
    fun deleteExercise(exercise: ExerciseEntity)
}