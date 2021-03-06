package com.revature.workitout.view.routineview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.revature.workitout.model.room.repo.RoutineRepo
import com.revature.workitout.view.GifLoader
import com.revature.workitout.viewmodel.RoutineVMFactory

@Composable
fun RoutineViewScreen(navController: NavController){
    val context = LocalContext.current
    val viewModel by lazy {
        ViewModelProvider(
            context as MainActivity,
            RoutineVMFactory(RoutineRepo(context.application))
        )
            .get(RoutineVM::class.java)
    }


    if(!viewModel.bDisplayExercise){

        //Display Routine list if no exercise is selected

        DisplayRoutineList(
            navController = navController,
            viewModel = viewModel
        )
    }
    else {

        //Display Exercise editor if one is selected
        ComponentEditor(
            exercise = viewModel.selectedComponent.value!!.toExerciseEntity(),
            setValue = viewModel.selectedComponent.value!!.Set,
            repValue = viewModel.selectedComponent.value!!.Rep,
            onBack = {viewModel.bDisplayExercise = false},
            onAccept = { set, rep->

                viewModel.selectedComponent.value!!.Set = set
                viewModel.selectedComponent.value!!.Rep = rep
                viewModel.updateComponent()
                viewModel.bDisplayExercise = false
            }
        )

    }





}
@Composable
fun RoutineComponentCard(
    exercise:RoutineComponent,
    viewModel: RoutineVM
){
    Card(
        shape =  RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                viewModel.selectedComponent.value = exercise
                viewModel.bDisplayExercise = true
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