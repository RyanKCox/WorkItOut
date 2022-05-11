package com.revature.workitout.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.decode.GifDecoder
import com.revature.workitout.model.retrofit.responses.Exercise
import com.revature.workitout.viewmodel.WorkoutListVM
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.revature.workitout.R
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModelProvider
import com.revature.workitout.MainActivity

@Composable
fun WorkoutList(navController: NavController){
    val context = LocalContext.current

    val viewModel = ViewModelProvider(context as MainActivity).get(WorkoutListVM::class.java)

    val exercises = viewModel.exerciseList.observeAsState(listOf())


    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Workouts")}
        )}
    )
    {

        val lazyState = rememberLazyListState()

        LazyColumn(
            state = lazyState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            if(viewModel.bLoading.value){

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

//                        Text("Loading")
                        CircularProgressIndicator()

                    }
                }

            }else if(viewModel.bLoadingFailed.value){

                item{
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Something went wrong!")

                    }
                }

            }
            else {
                item{
                    BodypartDropDown(viewModel)
                    Spacer(modifier = Modifier.size(10.dp))
                }

                items(exercises.value) { exercise ->

                    ExerciseCard(exercise,navController)

                }
            }
        }
    }
}
@Composable
fun BodypartDropDown(viewModel:WorkoutListVM){
    var bOpen by rememberSaveable{mutableStateOf(false)}
    val bodyparts = viewModel.getBodyparts().observeAsState()
    var sSelected by rememberSaveable{ mutableStateOf("All")}
    var nSize by remember{ mutableStateOf(Size.Zero)}
    val icon = if(bOpen)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(10.dp)){

        OutlinedTextField(
            value = sSelected,
            onValueChange = {sSelected = it},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coords ->
                    nSize = coords.size.toSize()
                }
                .clickable {
                    bOpen = !bOpen
                },
            label = {Text("BodyPart")},
            trailingIcon = {
                Icon(
                    icon,
                    "BodypartDrowdown",
//                    Modifier.clickable { bOpen = !bOpen }
                )
            },
            enabled = false
        )

        DropdownMenu(
            expanded = bOpen,
            onDismissRequest = {
                bOpen = false
                               },
            modifier = Modifier
                .width(with(LocalDensity.current){nSize.width.toDp()})
        ) {
            bodyparts.value?.forEach{part->
                DropdownMenuItem(
                    onClick = {
                        sSelected = part
                        if(sSelected == "All"){
                            viewModel.loadExercises()
                        }else {
                            viewModel.LoadBodyPartbyPart(sSelected)
                        }
                        bOpen = false
                    }
                ) {
                    Text(part)
                    
                }
            }

        }

    }

}

@Composable
fun ExerciseCard(exercise:Exercise,navController: NavController){


    Card(
        shape =  RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier.padding(10.dp)
    ) {
        Row {
            Image(
                painter = rememberImagePainter(
                    data = exercise.gifUrl,
                    builder = {
                        decoder(GifDecoder())
                        placeholder(R.drawable.workitout_logo)
                        crossfade(true)
                    }
                ),
                contentDescription = "exerciseGif",
                modifier = Modifier.size(150.dp)
            )
            Column {

                Text(
                    text = exercise.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text( "Body Part: ${exercise.bodyPart}")
                Text( "Target: ${exercise.target}")
                Text( "Equipment: ${exercise.equipment}")


            }
        }

    }

}