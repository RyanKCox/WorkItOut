package com.revature.workitout.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.model.retrofit.responses.Exercise
import com.revature.workitout.model.room.database.ExerciseDataBase
import com.revature.workitout.model.room.database.RoutineDatabase
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.entity.RoutineEntity
import com.revature.workitout.model.room.repo.RoutineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutineVM:ViewModel() {

    var routineList = MutableLiveData<List<RoutineEntity>>()
    var selectedRoutine:Int? = null
    var exerciseList = MutableLiveData<List<ExerciseEntity>>()

    init {
        routineList.postValue(listOf())
        exerciseList.postValue(listOf())
    }

    fun findIDByName(sName:String){
        routineList.value!!.forEach {
            if(it.name == sName) {
                selectedRoutine = it.id
                return@forEach
            }
        }

    }
    fun deleteRoutine(context: Context){
        if (selectedRoutine != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val routineRepo = RoutineRepo(context.applicationContext as Application)
                routineRepo.deleteRoutine(selectedRoutine!!)
            }
            loadRoutines(context)
        }
        selectedRoutine = if(routineList.value!= null){
            routineList.value!!.first().id
        }else
            null
    }
    fun createRoutine(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            val routineRepo = RoutineRepo(context.applicationContext as Application)
            var sName = "Test"
            sName += routineList.value!!.size.toString()
            routineRepo.addRoutine(RoutineEntity(
                id = 0,
                name = sName
            ))
        }
        loadRoutines(context)
    }

    fun loadRoutines(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            val routineDao = RoutineDatabase.getDataBase(context).routineDao()
            val exerciseDao = ExerciseDataBase.getDatabase(context).exerciseDao()

            val list = routineDao.fetchALlRoutines()
            routineList.postValue(list)

            if(list.isNotEmpty()) {
                selectedRoutine = list.first().id
                val componentList = routineDao.fetchRoutineComponentsById(selectedRoutine!!)

                val exeList = mutableListOf<ExerciseEntity>()
                componentList.forEach {
                    val exercise = exerciseDao.fetchExerciseById(it.ExerciseID)
                    exeList.add(exercise)
                }
                exerciseList.postValue(exeList)
            }
        }
    }
}