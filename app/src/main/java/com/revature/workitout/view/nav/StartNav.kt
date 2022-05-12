package com.revature.workitout.view.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.revature.workitout.view.AddToRoutine
import com.revature.workitout.view.MainMenu
import com.revature.workitout.view.SingleExercise
import com.revature.workitout.view.WorkoutList

@Composable
fun StartNav(navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = NavScreen.MainMenuScreen.route
    ){
        composable(NavScreen.MainMenuScreen.route){
            MainMenu(navController)
        }
        composable(NavScreen.WorkoutListScreen.route){
            WorkoutList(navController)
        }
        composable(NavScreen.SingleExerciseScreen.route){
            SingleExercise(navController)
        }
        composable(NavScreen.AddToRoutineScreen.route){
            AddToRoutine(navController)
        }
    }
}