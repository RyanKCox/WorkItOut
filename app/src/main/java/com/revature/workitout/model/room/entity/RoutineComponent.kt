package com.revature.workitout.model.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Components")
data class RoutineComponent(

    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val RoutineID:Long,

    var sName:String,
    var sTarget:String,
    var sBodypart:String,
    var sEquipment:String,
    var sGifUrl:String,

    var Set:Int,
    var Rep:Int

){
    fun toExerciseEntity():ExerciseEntity{
        return ExerciseEntity(
            sName =  sName,
            sTarget = sTarget,
            sBodypart = sBodypart,
            sEquipment = sEquipment,
            sGifUrl = sGifUrl
        )
    }
}