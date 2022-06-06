package com.revature.workitout.view.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.revature.workitout.viewmodel.utility.numInputLimit
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun EditSetRep(
    setValue : Int,
    repValue : Int?=null,
    onSetChange : (Int)->Unit = {},
    onRepChange :(Int)->Unit = {}
){

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        var sSet by rememberSaveable{ mutableStateOf(setValue.toString())}
        val customMod =
            if(repValue != null)
                Modifier.padding(10.dp).weight(1f)
            else
                Modifier.padding(10.dp)

        OutlinedTextField(
            value = sSet,
            onValueChange = {
                if (it != "") {
                    val change = numInputLimit(it.toInt())
                    sSet = change.toString()
                    onSetChange(change)
                } else {
                    onSetChange(0)
                    sSet = it
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = {
                Text(text = if(repValue == null) "Time:" else "Sets:")
            },
            modifier = customMod
        )

        if (repValue != null) {

            var sRep by rememberSaveable { mutableStateOf(repValue.toString()) }

            Text(
                text = " of "
            )

            OutlinedTextField(
                value = sRep,
                onValueChange = {
                    if (it != "") {
                        val change = numInputLimit(it.toInt())
                        sRep = change.toString()
                        onRepChange(change)
                    } else {
                        sRep = it
                        onRepChange(0)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = {
                    Text(text = "Reps:")
                },
                modifier = customMod
            )
        }
    }

}