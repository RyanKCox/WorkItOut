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
import com.revature.workitout.model.retrofit.responses.Exercise

@Composable
fun gifLoader(exercise:Exercise,modifier:Modifier = Modifier){
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
        modifier = modifier
    )
}
@Composable
fun onLoadingFail(){
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
fun onLoading(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(30.dp))
        CircularProgressIndicator()
    }
}
@Composable
fun DropDown(label:String,list:List<String>,subString:String,onValueChange:(String)->Unit){

    var bOpen by rememberSaveable{ mutableStateOf(false) }
    val icon = if(bOpen)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    var sSelected by rememberSaveable{ mutableStateOf(list.first())}

    Column(Modifier.padding(10.dp)) {
        OutlinedTextField(
            value = "$sSelected$subString",
            onValueChange ={sSelected = it},
            enabled = false,
            label = {Text(label)},
            trailingIcon = {
                Icon(icon,"TimerIcon")
            },
            modifier = Modifier
                .fillMaxWidth(.5f)
                .clickable {
                    bOpen = !bOpen
                }
        )
        DropdownMenu(
            expanded =bOpen,
            onDismissRequest = { bOpen = false },
            modifier = Modifier.fillMaxWidth(.5f)
        ) {
            list.forEach { time->
                DropdownMenuItem(
                    onClick = {
                        sSelected = time
                        onValueChange(sSelected)
//                        viewModel.sSet.value = time
                        bOpen = false

                    }
                ) {
                    Text("$time$subString")

                }
            }

        }

    }
}