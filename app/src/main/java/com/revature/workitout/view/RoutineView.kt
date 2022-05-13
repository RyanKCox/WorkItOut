package com.revature.workitout.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.revature.workitout.MainActivity
import com.revature.workitout.viewmodel.RoutineVM
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController

@Composable
fun RoutineViewScreen(navController: NavController){
    val context = LocalContext.current
    val viewModel = ViewModelProvider(context as MainActivity).get(RoutineVM::class.java)
    viewModel.loadRoutines(context)

    val routineList = viewModel.routineList.observeAsState()
    val exerciseList = viewModel.exerciseList.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Routines")
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.deleteRoutine(context)
                }
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "DeleteIcon")
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize()) {

            if (routineList.value != null) {
                if (routineList.value!!.isNotEmpty()) {

                    RoutineDropDown(viewModel)

                    Button(onClick = { viewModel.createRoutine(context) }) {
                        Text("Add Routine")

                    }

                    val lazyState = rememberLazyListState()
                    LazyColumn(
                        state = lazyState,
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(exerciseList.value!!) { exercise ->
                            ExerciseCard(exercise, navController, context)
                        }

                    }
                }
            }
        }
    }
}
@Composable
fun RoutineDropDown(viewModel:RoutineVM){

    var bOpen by rememberSaveable{ mutableStateOf(false)}
    val routines = viewModel.routineList.observeAsState()
    var sSelected by rememberSaveable{ mutableStateOf(routines.value!!.first().name)}
    var nSize by remember{ mutableStateOf(Size.Zero)}
    val icon = if(bOpen)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(horizontal = 10.dp)) {
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
            label = {
                Text(text = "Routine")
            },
            trailingIcon = {
                Icon(
                    icon,
                    "RoutineDropDown"
                )
            },
            enabled = false
        )
        DropdownMenu(
            expanded = bOpen,
            onDismissRequest = { bOpen = false },
            modifier = Modifier
                .width(with(LocalDensity.current){nSize.width.toDp()})
        ) {
            routines.value!!.forEach {
                DropdownMenuItem(
                    onClick = {
                        sSelected = it.name
                        viewModel.findIDByName(it.name)
                        bOpen = false
                    }
                ) {
                   Text(it.name)
                }
            }
        }
    }
}