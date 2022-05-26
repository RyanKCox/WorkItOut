package com.revature.workitout.view.routineview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.revature.workitout.model.constants.RoutineBuilder
import com.revature.workitout.view.GifLoader
import com.revature.workitout.viewmodel.RoutineVM
import com.revature.workitout.viewmodel.numInputLimit

@Composable
fun ComponentEditor(viewModel:RoutineVM){

    val lazyState = rememberLazyListState()
    val context = LocalContext.current
    var exercise by viewModel.selectedComponent

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text( exercise!!.sName)
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
                )
                }

                GifLoader(
                    webLink = exercise!!.sGifUrl,
                    modifier = Modifier
                        .size(display.dp)
                        .padding(40.dp)
                )

                var sSet by rememberSaveable {
                    mutableStateOf(exercise!!.Set.toString())
                }
                var sRep by rememberSaveable {
                    mutableStateOf(exercise!!.Rep.toString())
                }

                if(exercise!!.sBodypart == RoutineBuilder.EXERCISE_TYPE_CARDIO){

                    //Time for Cardio
                    OutlinedTextField(
                        value = sSet,
                        onValueChange = {
                            if(it != "") {
                                exercise!!.Set = numInputLimit(it.toInt())
                                sSet = exercise!!.Set.toString()
                            } else {
                                sSet = "1"
                                exercise!!.Set = 1
                            }
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
                            value = sSet,
                            onValueChange = {
                                if(it != "") {
                                    exercise!!.Set = numInputLimit(it.toInt())
                                    sSet = exercise!!.Set.toString()
                                } else {
                                    sSet = "1"
                                    exercise!!.Set = 1
                                }
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
                            value =sRep,
                            onValueChange = {
                                if(it != "") {
                                    exercise!!.Rep = numInputLimit(it.toInt())
                                    sRep = exercise!!.Rep.toString()
                                } else {
                                    sRep = "1"
                                    exercise!!.Rep = 1
                                }
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
                        viewModel.updateComponent()
                        viewModel.bDisplayExercise = false
                    }
                ) {
                    Text("Add")
                }
            }

        }
    }
    BackHandler {
        viewModel.bDisplayExercise = false
    }
}