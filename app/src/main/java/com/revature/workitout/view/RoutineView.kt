package com.revature.workitout.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.view.nav.NavScreen
import com.revature.workitout.viewmodel.SingleExerciseVM
import com.revature.workitout.viewmodel.WorkoutListVM

@Composable
fun RoutineViewScreen(navController: NavController){
    val context = LocalContext.current
    val viewModel = ViewModelProvider(context as MainActivity).get(RoutineVM::class.java)
    val selectedRoutine by remember{viewModel.selectedRoutine}

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
//        modifier = Modifier.fillMaxSize(),
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

                        val workoutVM = ViewModelProvider(context)
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
        var sName by rememberSaveable{ mutableStateOf("")}
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
@Composable
fun RoutineComponentCard(exercise:RoutineComponent, viewModel: RoutineVM){
    Card(
        shape =  RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier
            .padding(10.dp)
            .clickable {

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

@Composable
fun RoutineDropDown(viewModel:RoutineVM){

    var bOpen by rememberSaveable{ mutableStateOf(false)}
    val selectedRoutine by remember{viewModel.selectedRoutine}
    var sSelected = selectedRoutine?.routineEntity?.name ?: "Empty"
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
                .fillMaxWidth(.9f)
                .onGloballyPositioned { coords ->
                    nSize = coords.size.toSize()
                }
                .clickable {
                    if (!viewModel.routineList.isNullOrEmpty())
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
            if( !viewModel.routineList.isNullOrEmpty()) {
                viewModel.routineList!!.forEach {
                    DropdownMenuItem(
                        onClick = {
                            sSelected = it.routineEntity.name
                            viewModel.selectedRoutine.value = it
                            bOpen = false
                        }
                    ) {
                        Text(it.routineEntity.name)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuDropDown(viewModel: RoutineVM, modifier: Modifier = Modifier){
    var bOpen by rememberSaveable{ mutableStateOf(false)}

    Column(
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "RoutineMenu",
            modifier = modifier
                .clickable {
                    bOpen = !bOpen
                }
        )
        DropdownMenu(
            expanded = bOpen,
            onDismissRequest = { bOpen = false },
            modifier = Modifier.fillMaxWidth(.4f)
        ) {

            DropdownMenuItem(
                onClick = {
//                    viewModel.createRoutine()
                    viewModel.addRoutineDialog = true
                    bOpen = false
                }
            ) {
               Text("Add Routine")
            }
            DropdownMenuItem(
                onClick = {
                    viewModel.deleteRoutine()
                    bOpen = false
                }
            ) {
                Text("Delete Routine")
            }

        }

    }

}