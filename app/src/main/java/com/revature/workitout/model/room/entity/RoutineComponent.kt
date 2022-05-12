package com.revature.workitout.model.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Components")
data class RoutineComponent(

    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val RoutineID:Int,
    val ExerciseID:String,
    val Set:Int,
    val Rep:Int

)