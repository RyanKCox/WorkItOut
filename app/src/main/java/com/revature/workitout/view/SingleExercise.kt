package com.revature.workitout.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.revature.workitout.MainActivity
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.view.nav.NavScreen
import com.revature.workitout.viewmodel.SingleExerciseVM
import androidx.compose.runtime.getValue
import com.revature.workitout.viewmodel.utility.GifSizer

@Composable
fun SingleExercise(navController: NavController){

    val context = LocalContext.current

    val viewModel = ViewModelProvider(context as MainActivity).get(SingleExerciseVM::class.java)
    val lazyState = rememberLazyListState()
    val exercise = viewModel.exercise

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(exercise.sName)
                }
            )
        }
    ){

        LazyColumn(
            state = lazyState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {

                if(viewModel.bLoading) {
                    OnLoading()
                }
                else if (viewModel.bLoadingFailed) {
                    OnLoadingFail()
                }
                else{
                    val display by rememberSaveable{ mutableStateOf(GifSizer(context))}

                    GifLoader(
                        webLink = exercise.sGifUrl,
                        modifier = Modifier
                            .size(display.dp)
                            .padding(40.dp)
                    )

                    DisplayInfo(exercise)

                    Button(
                        onClick = {
                            //Send to builder
                            navController.navigate(NavScreen.AddToRoutineScreen.route)
                        }
                    ) {
                        Text(
                            text = "Add to Routine",
                            style = MaterialTheme.typography.body1
                        )

                    }
                }
            }
        }

    }
}
@Composable
fun DisplayInfo(exercise: ExerciseEntity){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(2.dp, Color.Black)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        ){
            Column(Modifier.weight(1f)) {
                Text(
                    "Body Part: ",
                    style = MaterialTheme.typography.body1)

            }
            Column(Modifier.weight(1f)) {
                Text(
                    exercise.sBodypart,
                    style = MaterialTheme.typography.body1)

            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        ){
            Column(Modifier.weight(1f)) {
                Text(
                    "Target Muscle: ",
                    style = MaterialTheme.typography.body1)

            }
            Column(Modifier.weight(1f)) {
                Text(
                    exercise.sTarget,
                    style = MaterialTheme.typography.body1)

            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        ){
            Column(Modifier.weight(1f)) {
                Text(
                    "Equipment: ",
                    style = MaterialTheme.typography.body1)

            }
            Column(Modifier.weight(1f)) {
                Text(
                    exercise.sEquipment,
                    style = MaterialTheme.typography.body1)

            }

        }
    }
}