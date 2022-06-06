package com.revature.workitout.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import com.revature.workitout.R
import com.revature.workitout.model.room.entity.ExerciseEntity

@Composable
fun GifLoader(webLink:String,modifier:Modifier = Modifier){
    Image(
        painter = rememberImagePainter(
            data = webLink,
            builder = {
                decoder(GifDecoder())
                placeholder(R.drawable.workitout_logo)
                crossfade(true)
            }
        ),
        contentDescription = "exerciseGif",
        modifier = modifier
    )
}
@Composable
fun OnLoadingFail(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(30.dp))
        Text("Something went wrong!")
    }
}
@Composable
fun OnLoading(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(30.dp))
        CircularProgressIndicator()
    }
}
