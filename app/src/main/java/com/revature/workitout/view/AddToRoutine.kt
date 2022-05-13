package com.revature.workitout.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.revature.workitout.MainActivity
import com.revature.workitout.model.constants.RoutineBuilder
import com.revature.workitout.view.nav.NavScreen
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
                            exercise.value!!.sName
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

                    GifLoader(
                        exercise = exercise.value!!,
                        modifier = Modifier
                            .size(display.dp)
                            .padding(40.dp)
                    )

                    if(exercise.value!!.sBodypart == RoutineBuilder.EXERCISE_TYPE_CARDIO){

                        //Time for Cardio
                        DropDown(
                            label = "Time",
                            list = viewModel.setList,
                            subString = " min."
                        ){
                            viewModel.sSet.value = it
                        }

                    }
                    else{
                        // rep/set count for other
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            DropDown(
                                label = "Sets",
                                list = viewModel.setList,
                                subString = " sets",
                                modifier = Modifier.weight(1f)
                            ){
                                viewModel.sSet.value = it
                            }

                            Text(
                                text = " of "
                            )

                            DropDown(
                                label = "Reps",
                                list = viewModel.repList,
                                subString = " reps",
                                modifier = Modifier.weight(1f)
                            ){
                                viewModel.sRep.value = it
                            }
                        }

                    }

                    Button(
                        onClick = {
                            navController.navigate(NavScreen.MainMenuScreen.route)
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}