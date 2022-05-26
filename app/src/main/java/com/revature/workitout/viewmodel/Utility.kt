package com.revature.workitout.viewmodel


fun makeUppercase(string:String):String{
    return string.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar {
            it.uppercaseChar()
        }
    }
}
fun numInputLimit(num:Int):Int{
    if(num > 99){
        return num % 100
    }
    return num
}