package com.revature.workitout.model.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.revature.workitout.model.room.entity.Routine
import com.revature.workitout.model.room.entity.RoutineComponent

@Dao
interface RoutineDAO {

    @Query("SELECT * FROM Routines")
    fun fetchALlRoutines():LiveData<List<Routine>>

    @Query("SELECT * FROM Components WHERE RoutineID = :id")
    suspend fun fetchRoutine(id:Int):LiveData<List<RoutineComponent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine:Routine):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComponent(component:RoutineComponent):Long

    @Delete
    suspend fun deleteRoutine(routine:Routine)

    @Delete
    suspend fun deleteComponent(component:RoutineComponent)
}