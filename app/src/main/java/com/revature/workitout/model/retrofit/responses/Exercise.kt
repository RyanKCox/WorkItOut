package com.revature.workitout.model.retrofit.responses

import com.google.gson.annotations.SerializedName

data class Exercise(

    @SerializedName("id")
    val id:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("target")
    val target:String,
    @SerializedName("bodyPart")
    val bodyPart:String,
    @SerializedName("equipment")
    val equipment:String,
    @SerializedName("gifUrl")
    var gifUrl:String

)
