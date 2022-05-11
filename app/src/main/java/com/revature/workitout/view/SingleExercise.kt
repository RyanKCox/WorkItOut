package com.revature.workitout.view

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.revature.workitout.MainActivity
import com.revature.workitout.viewmodel.SingleExerciseVM

@Composable
fun SingleExercise(navController: NavController){

    val context = LocalContext.current

    val viewModel = ViewModelProvider(context as MainActivity).get(SingleExerciseVM::class.java)
    val lazyState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Exercise")}
            )
        }
    ){

    }
}