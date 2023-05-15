package com.example.zty.http

import com.example.zty.entity.ResponseData
import com.example.zty01.entity.TypeEntity
import com.example.zty01.entity.VideoEntity
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiSevice {

    @GET("/videotype/getSimpleType")
    suspend fun getSimpleType():ResponseData<List<TypeEntity>>

    @GET("/videosimple/getSimpleVideoByChannelId")
    suspend fun getSimpleVideoByChannelId(@Query("channelId")channelId:String,
                                          @Query("page")page:Int,
                                          @Query("pagesize")pagesize:Int):ResponseData<List<VideoEntity>>




}