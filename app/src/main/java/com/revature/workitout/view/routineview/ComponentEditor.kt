package com.revature.workitout.view.routineview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.revature.workitout.model.constants.RoutineBuilder
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.view.DisplayInfo
import com.revature.workitout.view.GifLoader
import com.revature.workitout.view.shared.EditSetRep
import com.revature.workitout.viewmodel.utility.GifSizer
import com.revature.workitout.viewmodel.RoutineVM

@Composable
fun ComponentEditor(
    exercise: ExerciseEntity,
    setValue:Int,
    repValue:Int,
    onBack:()->Unit,
    onAccept:( set:Int,rep:Int )->Unit
){

    val lazyState = rememberLazyListState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(exercise.sName)
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

                val display by rememberSaveable{ mutableStateOf( GifSizer(context))}

                GifLoader(
                    webLink = exercise.sGifUrl,
                    modifier = Modifier
                        .size(display.dp)
                        .padding(40.dp)
                )
                DisplayInfo(exercise)

                var nSet by rememberSaveable{ mutableStateOf(setValue)}
                var nRep by rememberSaveable{ mutableStateOf(repValue)}

                var bDisplayInputError by rememberSaveable{ mutableStateOf(false)}

                if(bDisplayInputError){
                    Text(
                        text = "Set or Rep cannot be 0 or empty",
                        color = Color.Red
                    )
                }

                if(exercise.sBodypart == RoutineBuilder.EXERCISE_TYPE_CARDIO){
                    //Time for Cardio

                    EditSetRep(
                        setValue = nSet,
                        onSetChange = {nSet = it}
                    )
                }
                else{
                    // rep/set count for other

                    EditSetRep(
                        setValue = nSet,
                        repValue = nRep,
                        onSetChange = { nSet = it },
                        onRepChange = {nRep = it}
                    )
                }

                Button(
                    onClick = {
                        if(nSet == 0 || nRep == 0){
                            //display error
                            bDisplayInputError = true
                        }
                        else {

                            onAccept(nSet,nRep)
                        }
                    }
                ) {
                    Text("Done")
                }
            }

        }
    }
    BackHandler {
        onBack()
    }
}