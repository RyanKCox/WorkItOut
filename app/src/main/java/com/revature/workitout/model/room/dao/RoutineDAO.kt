package com.revature.workitout.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.revature.workitout.model.room.entity.RoutineEntity
import com.revature.workitout.model.room.entity.RoutineComponent

@Dao
interface RoutineDAO {

    @Query("SELECT * FROM Routines")
    fun fetchALlRoutines():List<RoutineEntity>

    @Query("SELECT * FROM Components WHERE RoutineID = :id")
    fun fetchRoutineComponentsById(id:Int):List<RoutineComponent>

    @Query("SELECT * FROM Routines WHERE id = :id")
    fun fetchRoutineById(id:Int):RoutineEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoutine(routine:RoutineEntity):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComponent(component:RoutineComponent):Long

    @Delete
    fun deleteRoutine(routine:RoutineEntity)

    @Delete
    fun deleteComponent(component:RoutineComponent)
}