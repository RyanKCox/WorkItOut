package com.revature.workitout.model.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="ExerciseList")
data class ExerciseEntity(

    @PrimaryKey(autoGenerate = true)
    val id:Int,
    var sName:String,
    var sTarget:String,
    var sBodypart:String,
    var sEquipment:String,
    var sGifUrl:String
)