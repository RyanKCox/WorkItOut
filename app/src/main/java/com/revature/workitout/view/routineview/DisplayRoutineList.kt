package com.revature.workitout.view.routineview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.revature.workitout.MainActivity
import com.revature.workitout.view.MenuDropDown
import com.revature.workitout.view.RoutineComponentCard
import com.revature.workitout.view.RoutineDropDown
import com.revature.workitout.view.nav.NavScreen
import com.revature.workitout.viewmodel.RoutineVM
import com.revature.workitout.viewmodel.WorkoutListVM

@Composable
fun DisplayRoutineList(
    navController: NavController,
    viewModel:RoutineVM
){

    val selectedRoutine by viewModel.selectedRoutine

    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("Routines")
                }
            )
        },
        floatingActionButton = {
            if(selectedRoutine != null) {
                FloatingActionButton(
                    onClick = {

                        val workoutVM = ViewModelProvider(context as MainActivity)
                            .get(WorkoutListVM::class.java)
                        workoutVM.routineID = selectedRoutine!!.routineEntity.id

                        navController.navigate(NavScreen.WorkoutListScreen.route)

                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "AddExerciseIcon")
                }
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.size(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min)
            ){

                RoutineDropDown(viewModel)

                MenuDropDown(
                    viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 5.dp)
                )
            }

            if(selectedRoutine != null){

                val lazyState = rememberLazyListState()
                LazyColumn(
                    state = lazyState,
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(selectedRoutine!!.exercises) { exercise ->
                        RoutineComponentCard(exercise, viewModel)
                    }
                }
            }
        }
    }
    if(viewModel.addRoutineDialog){
        var sName by rememberSaveable{ mutableStateOf("") }
        AlertDialog(
            onDismissRequest = {viewModel.addRoutineDialog = false},
            title = { Text("Name of Routine?") },
            text = {
                OutlinedTextField(
                    value = sName,
                    onValueChange = { sName = it }
                )
            },
            buttons = {

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.createRoutine(sName)
                            viewModel.addRoutineDialog = false
                        }
                    ) {
                        Text("Accept")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.addRoutineDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

}