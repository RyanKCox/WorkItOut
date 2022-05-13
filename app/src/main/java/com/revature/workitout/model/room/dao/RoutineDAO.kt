package com.revature.workitout.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.revature.workitout.model.room.entity.Routine
import com.revature.workitout.model.room.entity.RoutineComponent

@Dao
interface RoutineDAO {

    @Query("SELECT * FROM Routines")
    fun fetchALlRoutines():LiveData<List<Routine>>

    @Query("SELECT * FROM Components WHERE RoutineID = :id")
    fun fetchRoutine(id:Int):LiveData<List<RoutineComponent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoutine(routine:Routine):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComponent(component:RoutineComponent):Long

    @Delete
    fun deleteRoutine(routine:Routine)

    @Delete
    fun deleteComponent(component:RoutineComponent)
}