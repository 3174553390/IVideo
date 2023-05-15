package com.example.ivideo.model

import com.example.ivideo.entity.ResponseData
import com.example.ivideo.entity.SimpleTypeEntity
import com.example.ivideo.entity.VideoEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServer {
    @GET("/videosimple/getSimpleVideoByChannelId")
    suspend fun getSimpleVideoByChannelId(@Query("channelId") channelId:String,
                                          @Query("page") page:Int,
                                          @Query("pagesize") pagesize:Int): ResponseData<List<VideoEntity>>
    //视频类型
    @GET("/videotype/getSimpleType")
    suspend fun getSimpleType():ResponseData<List<SimpleTypeEntity>>
}