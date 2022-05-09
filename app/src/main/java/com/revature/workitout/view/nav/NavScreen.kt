package com.revature.workitout.view.nav

sealed class NavScreen(
    val route:String
){
    object MainMenuScreen:NavScreen("MainMenu")
    object WorkoutListScreen:NavScreen("WorkoutList")

}
