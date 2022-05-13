package com.revature.workitout.model.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Routines")
data class RoutineEntity (

    @PrimaryKey(autoGenerate = true)
    val id:Int,

    val name:String
)