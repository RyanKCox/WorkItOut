package com.revature.workitout.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.revature.workitout.R
import com.revature.workitout.view.nav.NavScreen

@Composable
fun MainMenu(navController: NavController){


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Main Menu") })
        }
    ) {
        val lazyState = rememberLazyListState()
        LazyColumn(
            state = lazyState,
            modifier = Modifier.fillMaxSize()
        ){
            item {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.size(50.dp))

                    Image(
                        painter = painterResource(id = R.drawable.workitout_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.size(80.dp))

                    Button(
                        onClick = {
                            navController.navigate(NavScreen.RoutineViewScreen.route)
//                            navController.navigate(NavScreen.WorkoutListScreen.route)

                        },
                        modifier = Modifier
                            .fillMaxWidth(.4f)
                            .fillMaxHeight(.1f)
                    ) {
                        Text(text = "Enter")

                    }

                }
            }
        }

    }
}