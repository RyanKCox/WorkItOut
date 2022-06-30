package com.revature.workitout.view.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.revature.workitout.R
import com.revature.workitout.view.nav.NavScreen

@Composable
fun WIOScaffold(
    sTitle:String,
    navController: NavController,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    Body:@Composable () -> Unit)
{
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
                 OutDrawer(
                     scope = scope,
                     scaffoldState = scaffoldState,
                     sTitle = sTitle)
//            TopAppBar(
//                title = {
//                    Text(sTitle)
//                }
//            )
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
        drawerContent = {
            InDrawer(
                navController = navController,
                scope = scope,
                scaffoldState = scaffoldState)
        }

    ) {
        Body()
    }


}

@Composable
fun OutDrawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    sTitle:String
){
    TopAppBar(
        navigationIcon = {
           IconButton(
               onClick = {
                   scope.launch { scaffoldState.drawerState.open() }
               }) {
                   Icon(
                       painter = painterResource(id = R.drawable.ic_bookblack),
                       contentDescription = null
                   )
           }
        },
        title = {
            Text(
                sTitle,
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
            )
        },
        backgroundColor = MaterialTheme.colors.background
    )
}
@Composable
fun InDrawer(
    navController: NavController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
){
//    val context = LocalContext.current
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize(.5f)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item{
            Text(
                text = "Routines",
                style = MaterialTheme.typography.subtitle1,

                modifier = Modifier
                    .clickable {
                        scope.launch{
                            navController.navigate(NavScreen.RoutineViewScreen.route)
                            scaffoldState.drawerState.close()
                        }
                    }
                    .padding(10.dp)
                    .fillMaxWidth(.9f)
            )
            Spacer(Modifier.size(20.dp))
        }

        item{
            Text(
                text = "Gym Locator",
                style = MaterialTheme.typography.subtitle1,

                modifier = Modifier
                    .clickable {
                        scope.launch{
                            navController.navigate(NavScreen.GymLocatorScreen.route)
                            scaffoldState.drawerState.close()
                        }
                    }
                    .padding(10.dp)
                    .fillMaxWidth(.9f)
            )
            Spacer(Modifier.size(20.dp))

        }
    }
}
