package com.revature.workitout.viewmodel

import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.revature.workitout.model.constants.RoutineBuilder
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.repo.IRoutineRepo
import com.revature.workitout.model.room.repo.RoutineRepo
import com.revature.workitout.viewmodel.providers.CoroutineProvider
import kotlinx.coroutines.launch

class RunRoutineVM(
    private val routineRepo: IRoutineRepo,
    private val coroutineProvider: CoroutineProvider
):ViewModel() {
    var activeRoutine:MutableState <Routine?> = mutableStateOf(null)
    var activeExercise:MutableState<RoutineComponent?> = mutableStateOf(null)
    var exerciseIndex = mutableStateOf(0)
    var setCount = mutableStateOf(1)
    var isCardio = mutableStateOf(false)
    var progress = mutableStateOf(0f)
    var countDownTimer:CountDownTimer? = null
    var curTime = mutableStateOf(0L)
    var bStartTimer = mutableStateOf(true)


    fun loadWorkout(routineID:Long){
        viewModelScope.launch(coroutineProvider.io){
            activeRoutine.value = routineRepo.getRoutineById(routineID)
            exerciseIndex.value = 0
            setCount.value = 1
//            activeExercise.value = activeRoutine.value!!.exercises[exerciseIndex.value]
            loadNextExercise()
        }
    }

    fun progressExercise():Boolean{
        setCount.value++
        if(setCount.value > activeExercise.value!!.Set)
        {
            setCount.value = 1
            exerciseIndex.value++
            if(exerciseIndex.value >= activeRoutine.value!!.exercises.size)
            {
                //true result for navigation
                return true
            }
            else
            {
//                activeExercise.value = activeRoutine.value!!.exercises[exerciseIndex.value]
                loadNextExercise()
            }
        }
        return false
    }
    private fun loadNextExercise(){
        activeExercise.value = activeRoutine.value!!.exercises[exerciseIndex.value]
        isCardio.value = activeExercise.value!!.sBodypart == RoutineBuilder.EXERCISE_TYPE_CARDIO
        if(isCardio.value) {
            setCount.value = 0
            createTimer()
        }
        progress.value = 0f
    }
    fun startTimer(){

        countDownTimer?.start()
        bStartTimer.value = false
    }
    fun pauseTimer(){
        countDownTimer?.cancel()
        bStartTimer.value = true
    }
    private fun createTimer(){
        val totalTime = activeExercise.value!!.Set * 60000L
        countDownTimer =
            object: CountDownTimer(totalTime,1000){
                override fun onTick(milliseconds: Long) {
                    progress.value = milliseconds.toFloat() / totalTime
                    curTime.value = milliseconds
                }
                override fun onFinish() {
                    progress.value = 0f
                    curTime.value = 0
                }
            }

    }

}

class RunRoutineVMFactory(private val routineRepo: RoutineRepo)
    :ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RunRoutineVM::class.java)){
            return RunRoutineVM(
                routineRepo = routineRepo,
                coroutineProvider = CoroutineProvider()
            ) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }

}