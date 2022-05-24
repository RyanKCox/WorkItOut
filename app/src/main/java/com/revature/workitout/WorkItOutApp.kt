package com.revature.workitout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.revature.workitout.ui.theme.WorkItOutTheme
import com.revature.workitout.view.nav.StartNav

@Composable
fun WorkItOutApp(
    widthSizeClass: WindowWidthSizeClass
){

    val navController = rememberNavController()

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    WorkItOutTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            StartNav(navController = navController)
        }
    }

}