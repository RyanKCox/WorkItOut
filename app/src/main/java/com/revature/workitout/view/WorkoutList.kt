package com.revature.workitout.view

import android.content.Context
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
import com.revature.workitout.viewmodel.WorkoutListVM
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModelProvider
import com.revature.workitout.MainActivity
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.view.nav.NavScreen
import com.revature.workitout.viewmodel.SingleExerciseVM

@Composable
fun WorkoutList(navController: NavController){
    val context = LocalContext.current

    val viewModel = ViewModelProvider(context as MainActivity).get(WorkoutListVM::class.java)

    val exercises by viewModel.exerciseList.observeAsState(listOf())


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
                    OnLoading()
                }

            }
            else if(viewModel.bLoadingFailed.value){

                item{
                    OnLoadingFail()
                }

            }
            else {
                item{
                    BodypartDropDown(viewModel)
                    Spacer(modifier = Modifier.size(10.dp))
                }

                items(exercises) { exercise ->

                    ExerciseCard(exercise,navController,context,viewModel)

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
                    "BodypartDrowdown"
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
                            viewModel.loadExerciseByBodypart(sSelected)
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
fun ExerciseCard(
    exercise:ExerciseEntity,
    navController: NavController,
    context: Context,
    viewModel:WorkoutListVM
){



    Card(
        shape =  RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                val singleVM =
                    ViewModelProvider(context as MainActivity)
                        .get(SingleExerciseVM::class.java)
                singleVM.loadExercise(exercise.id,context)
                singleVM.routineID = viewModel.routineID
                navController.navigate(NavScreen.SingleExerciseScreen.route)

            }
    ) {
        Row {

            GifLoader(exercise.sGifUrl,Modifier.size(150.dp))

            Column {

                Text(
                    text = exercise.sName,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    "Body Part: ${exercise.sBodypart}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    "Target: ${exercise.sTarget}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    "Equipment: ${exercise.sEquipment}",
                    style = MaterialTheme.typography.body1
                )


            }
        }

    }

}