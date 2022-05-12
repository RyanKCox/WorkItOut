package com.revature.workitout.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.revature.workitout.MainActivity
import com.revature.workitout.viewmodel.SingleExerciseVM

@Composable
fun AddToRoutine(navController: NavController) {

    val context = LocalContext.current

    val viewModel = ViewModelProvider(context as MainActivity).get(SingleExerciseVM::class.java)
    val lazyState = rememberLazyListState()
    val exercise = viewModel.exercise.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (exercise.value != null)
                            exercise.value!!.name
                        else
                            "Exercise"
                    )
                }
            )
        }
    ) {

        LazyColumn(
            state = lazyState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item{

                if(exercise.value != null) {
                    val display =
                        if(context.resources.displayMetrics.widthPixels < context.resources.displayMetrics.heightPixels)
                            context.resources.displayMetrics.widthPixels/context.resources.displayMetrics.density
                        else
                            context.resources.displayMetrics.heightPixels/context.resources.displayMetrics.density

                    gifLoader(
                        exercise = exercise.value!!,
                        modifier = Modifier
                            .size(display.dp)
                            .padding(40.dp)
                    )

                    if(exercise.value!!.bodyPart == "cardio"){
                        //Time for Cardio
                        DropDown(
                            label = "Time",
                            list = listOf("5", "10", "15", "20", "25", "30"),
                            subString = " min."
                        ){
                            viewModel.sSet.value = it
                        }
                        Text(viewModel.sSet.value)

                    }
                    else{
                        // rep/set count for other

                    }
                }
            }
        }
    }
}