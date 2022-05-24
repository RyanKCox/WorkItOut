package com.revature.workitout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.revature.workitout.model.room.repo.ExerciseRepo
import com.revature.workitout.model.room.repo.RoutineRepo
import com.revature.workitout.ui.theme.WorkItOutTheme
import com.revature.workitout.view.MainMenu
import com.revature.workitout.view.nav.StartNav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)

        RepositoryManager.routineRepo = RoutineRepo(this.application)
        RepositoryManager.exerciseRepo = ExerciseRepo(this.application)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            WorkItOutApp(widthSizeClass)
        }
    }
}
