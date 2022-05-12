package com.revature.workitout.model.retrofit.responses

import com.google.gson.annotations.SerializedName

data class Exercise(

    @SerializedName("id")
    val id:String,
    @SerializedName("name")
    var name:String,
    @SerializedName("target")
    var target:String,
    @SerializedName("bodyPart")
    var bodyPart:String,
    @SerializedName("equipment")
    var equipment:String,
    @SerializedName("gifUrl")
    var gifUrl:String

)
