package com.revature.workitout.model.data

import androidx.room.Embedded
import androidx.room.Relation
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.entity.RoutineEntity

data class Routine(

    @Embedded val routineEntity: RoutineEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "RoutineID"
    )
    val exercises:List<RoutineComponent>
)
