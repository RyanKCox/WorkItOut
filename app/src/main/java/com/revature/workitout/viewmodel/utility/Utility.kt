package com.revature.workitout.viewmodel.utility

import android.content.Context


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
fun GifSizer(context:Context):Float{

    return if(context.resources.displayMetrics.widthPixels < context.resources.displayMetrics.heightPixels)
        context.resources.displayMetrics.widthPixels/context.resources.displayMetrics.density
    else
        context.resources.displayMetrics.heightPixels/context.resources.displayMetrics.density
}