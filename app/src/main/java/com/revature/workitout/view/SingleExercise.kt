package com.revature.workitout.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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

@Composable
fun SingleExercise(navController: NavController){

    val context = LocalContext.current

    val viewModel = ViewModelProvider(context as MainActivity).get(SingleExerciseVM::class.java)
    val lazyState = rememberLazyListState()
    val exercise = viewModel.exercise.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                            if(exercise.value!=null)
                                exercise.value!!.sName
                            else
                                "Exercise"
                    )
                }
            )
        }
    ){
        if(!viewModel.bLoading.value && !viewModel.bLoadingFailed.value) {

            LazyColumn(
                state = lazyState,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {

                    if(exercise.value != null) {
                        val display =
                            if(context.resources.displayMetrics.widthPixels < context.resources.displayMetrics.heightPixels)
                                context.resources.displayMetrics.widthPixels/context.resources.displayMetrics.density
                        else
                                context.resources.displayMetrics.heightPixels/context.resources.displayMetrics.density

                        GifLoader(
                            webLink = exercise.value!!.sGifUrl,
                            modifier = Modifier
                                .size(display.dp)
                                .padding(40.dp)
                        )

                        DisplayInfo(exercise.value!!)

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
                    else if(viewModel.bLoading.value){
                        OnLoading()

                    }
                    else{
                        OnLoadingFail()
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
                .padding(10.dp)
        ){
            Column(Modifier.fillMaxWidth(.5f)) {
                Text(
                    "Body Part: ",
                    style = MaterialTheme.typography.body1)

            }
            Column(Modifier.fillMaxWidth()) {
                Text(
                    exercise.sBodypart,
                    style = MaterialTheme.typography.body1)

            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            Column(Modifier.fillMaxWidth(.5f)) {
                Text(
                    "Target Muscle: ",
                    style = MaterialTheme.typography.body1)

            }
            Column(Modifier.fillMaxWidth()) {
                Text(
                    exercise.sTarget,
                    style = MaterialTheme.typography.body1)

            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            Column(Modifier.fillMaxWidth(.5f)) {
                Text(
                    "Equipment: ",
                    style = MaterialTheme.typography.body1)

            }
            Column(Modifier.fillMaxWidth()) {
                Text(
                    exercise.sEquipment,
                    style = MaterialTheme.typography.body1)

            }

        }
    }
}