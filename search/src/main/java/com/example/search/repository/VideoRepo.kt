package com.example.zty01.repository

import com.example.zty.entity.ResponseData
import com.example.zty.http.RetrofitManager
import com.example.zty01.entity.TypeEntity
import com.example.zty01.entity.VideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoRepo {
    val apiService= RetrofitManager.getApiService()

    suspend fun getSimpleType(): ResponseData<List<TypeEntity>> {
        return withContext(Dispatchers.IO){
            var list=apiService.getSimpleType()
            list
        }
    }

    suspend fun getSimpleVideoByChannelId(channelId:String,page:Int,pagesize:Int): ResponseData<List<VideoEntity>> {
        return withContext(Dispatchers.IO){
            var list=apiService.getSimpleVideoByChannelId(channelId,page, pagesize)
            list
        }
    }

}