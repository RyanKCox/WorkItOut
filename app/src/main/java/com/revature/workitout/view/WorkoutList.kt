package com.revature.workitout.view

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.revature.workitout.model.retrofit.allexcercises.Exercise
import com.revature.workitout.viewmodel.WorkoutListVM
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.revature.workitout.R

@Composable
fun WorkoutList(navController: NavController){
    val viewmodel = WorkoutListVM()

    val exercises = viewmodel.getExerciseList().observeAsState(listOf())


    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Workouts")}
        )}
    )
    {

        val lazyState = rememberLazyListState()

        LazyColumn(
            state = lazyState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            if(viewmodel.bLoading.value){

                item {
                    Text("Loading")
                }

            } else {

                items(exercises.value) { exercise ->

                    ExerciseCard(exercise)

                }
            }
        }
    }
}
@Composable
fun ExerciseCard(exercise:Exercise){


    Card(
        shape =  RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier.padding(10.dp)
    ) {
        Row {
            Image(
                painter = rememberImagePainter(
                    data = exercise.gifUrl,
                    builder = {
                        decoder(GifDecoder())
                        placeholder(R.drawable.workitout_logo)
                        crossfade(true)
                    }
                ),
                contentDescription = "exerciseGif",
                modifier = Modifier.size(150.dp)
            )
//            AsyncImage(
//                model = exercise.gifUrl,
//                contentDescription = "ExerciseGif",
//                imageLoader = imageLoader,
//                modifier = Modifier.size(150.dp)
//            )
            Column {

                Text(
                    text = exercise.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text( "Body Part: ${exercise.bodyPart}")
                Text( "Target: ${exercise.target}")
                Text( "Equipment: ${exercise.equipment}")


            }
        }

    }

}