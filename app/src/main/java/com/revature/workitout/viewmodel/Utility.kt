package com.revature.workitout.viewmodel


fun makeUppercase(string:String):String{
    return string.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar {
            it.uppercaseChar()
        }
    }
}