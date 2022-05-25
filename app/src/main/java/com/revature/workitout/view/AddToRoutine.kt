package com.revature.workitout.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.revature.workitout.MainActivity
import com.revature.workitout.model.constants.RoutineBuilder
import com.revature.workitout.view.nav.NavScreen
import com.revature.workitout.viewmodel.SingleExerciseVM
import androidx.compose.runtime.getValue

@Composable
fun AddToRoutine(navController: NavController) {

    val context = LocalContext.current

    val viewModel = ViewModelProvider(context as MainActivity).get(SingleExerciseVM::class.java)
    val lazyState = rememberLazyListState()
    val exercise = viewModel.exercise

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text( exercise.sName)
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

                    val display by rememberSaveable{ mutableStateOf(
                        if(context.resources.displayMetrics.widthPixels < context.resources.displayMetrics.heightPixels)
                            context.resources.displayMetrics.widthPixels/context.resources.displayMetrics.density
                        else
                            context.resources.displayMetrics.heightPixels/context.resources.displayMetrics.density
                    )}

                    GifLoader(
                        webLink = exercise.sGifUrl,
                        modifier = Modifier
                            .size(display.dp)
                            .padding(40.dp)
                    )

                    if(exercise.sBodypart == RoutineBuilder.EXERCISE_TYPE_CARDIO){

                        //Time for Cardio
                        OutlinedTextField(
                            value = viewModel.nSet.toString(),
                            onValueChange = {
                                if(it != "") {
                                    viewModel.nSet = viewModel.numInputLimit(it.toInt())
                                } else
                                    viewModel.nSet = 1
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            label = {
                                Text(text = "Sets:")
                            }
                        )

                    }
                    else{
                        // rep/set count for other
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            OutlinedTextField(
                                value = viewModel.nSet.toString(),
                                onValueChange = {
                                    if(it != "") {
                                        viewModel.nSet = viewModel.numInputLimit(it.toInt())
                                    } else
                                        viewModel.nSet = 1
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                label = {
                                    Text(text = "Sets:")
                                },
                                modifier = Modifier
                                    .padding(5.dp)
                                    .weight(1f)
                            )

                            Text(
                                text = " of "
                            )

                            OutlinedTextField(
                                value =viewModel.nRep.toString(),
                                onValueChange = {
                                    if(it != "") {
                                        viewModel.nRep = viewModel.numInputLimit(it.toInt())
                                    } else
                                        viewModel.nRep = 1
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                label = {
                                    Text(text = "Reps:")
                                },
                                modifier = Modifier
                                    .padding(5.dp)
                                    .weight(1f)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.addExercise()
                            navController.popBackStack(NavScreen.RoutineViewScreen.route,false)
                        }
                    ) {
                        Text("Add")
                    }
                }

        }
    }
}