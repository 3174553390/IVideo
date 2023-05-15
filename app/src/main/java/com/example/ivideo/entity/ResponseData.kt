package com.example.ivideo.entity

data class ResponseData<T>(
    var code:Int,
    var msg:String,
    var data:T?
)