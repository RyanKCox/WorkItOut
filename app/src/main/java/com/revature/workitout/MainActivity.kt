package com.revature.workitout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import com.revature.workitout.model.room.repo.ExerciseRepo
import com.revature.workitout.model.room.repo.RoutineRepo

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,true)

        RepositoryManager.routineRepo = RoutineRepo(this.application)
        RepositoryManager.exerciseRepo = ExerciseRepo(this.application)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            WorkItOutApp(widthSizeClass)
        }
    }
}
