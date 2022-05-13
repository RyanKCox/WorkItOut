package com.revature.workitout.view.nav

sealed class NavScreen(
    val route:String
){
    object MainMenuScreen:NavScreen("MainMenu")
    object WorkoutListScreen:NavScreen("WorkoutList")
    object SingleExerciseScreen:NavScreen("SingleExercise")
    object AddToRoutineScreen:NavScreen("AddToRoutine")
    object RoutineViewScreen:NavScreen("RoutineView")

}
