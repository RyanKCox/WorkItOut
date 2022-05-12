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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.revature.workitout.MainActivity
import com.revature.workitout.model.retrofit.responses.Exercise
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
                                exercise.value!!.name
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

                        gifLoader(
                            exercise = exercise.value!!,
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
                        onLoading()

                    }
                    else{
                        onLoadingFail()
                    }

                }
            }
        }
    }
}
@Composable
fun DisplayInfo(exercise: Exercise){

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
                    exercise.bodyPart,
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
                    exercise.target,
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
                    exercise.equipment,
                    style = MaterialTheme.typography.body1)

            }

        }
    }
}
@Preview
@Composable
fun previewDisplayInfo(){
    DisplayInfo(
        Exercise(
            id = "0001",
            name = "test",
            target = "Chesttest longtestchest",
            bodyPart = "Another Long Test",
            equipment = "Big Equipment",
            gifUrl = "none"
        )
    )
}