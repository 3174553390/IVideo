package com.example.zty.entity

data class ResponseData<T> (
    val code:Int,
    val msg:String,
    val data:T?

)
