package com.revature.workitout.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.revature.workitout.MainActivity
import com.revature.workitout.view.nav.NavScreen
import com.revature.workitout.viewmodel.RunRoutineVM
import com.revature.workitout.viewmodel.utility.GifSizer

@Composable
fun RunRoutine(navController: NavController){
    val context = LocalContext.current
    val viewModel by lazy {
        ViewModelProvider(context as MainActivity).get(RunRoutineVM::class.java)
    }
    
    val scaffoldState = rememberScaffoldState()
    var topBarTitle by rememberSaveable{ mutableStateOf("WorkOut!")}
    
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(topBarTitle)
                }
            )
        }
    ) {
        
        val routine by viewModel.activeRoutine
        
        if(routine == null){
            //something went wrong
            OnLoadingFail()
        }
        else{
            topBarTitle = routine!!.routineEntity.name
            
            val exercise by viewModel.activeExercise
            val setCount by viewModel.setCount
            val isCardio by viewModel.isCardio
            
            if(exercise != null){
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = exercise!!.sName,
                        style = MaterialTheme.typography.subtitle1
                    )

                    val display by rememberSaveable{ mutableStateOf(GifSizer(context))}
                    GifLoader(
                        webLink = exercise!!.sGifUrl,
                        modifier = Modifier
                            .size(display.dp)
                            .padding(20.dp)
                    )
                    if(isCardio){

                        val progress by viewModel.progress
                        val bStart by viewModel.bStartTimer
                        Box(contentAlignment = Alignment.Center) {
                            LinearProgressIndicator(
                                modifier = Modifier.height(40.dp),
                                progress = progress,
                                color = Color.Green,
                                backgroundColor = Color.Yellow

                            )
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = "${viewModel.curTime.value/60000}:${(viewModel.curTime.value%60000)/1000}")
                            }
                        }

                        Text(
                            text = "Duration: ${exercise!!.Set} min"
                        )
                        Button(
                            onClick = {
                                if(bStart)
                                    viewModel.startTimer()
                                else
                                    viewModel.pauseTimer()
                            }
                        ) {
                            Text(text = if(bStart) "Start" else "Pause")
                        }

                    }
                    else {
                        Text(
                            text = "Set: $setCount of ${exercise!!.Set} - Reps: ${exercise!!.Rep}"
                        )
                    }

                    Button(
                        onClick = {
                            if (viewModel.progressExercise()) {
                                navController.navigate(NavScreen.RoutineViewScreen.route)
                            }
                        }
                    ) {
                        Text(text = "Next")
                    }
                }
            }
        }
    }
}