package com.example.ivideo.reposities

import com.example.ivideo.entity.ResponseData
import com.example.ivideo.entity.SimpleTypeEntity
import com.example.ivideo.entity.VideoEntity
import com.example.ivideo.model.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoRePo {
    val apiServer = RetrofitManager.getApiServer()
    suspend fun getSimpleVideoByChannelId(channelId:String,page:Int,pagesize:Int): ResponseData<List<VideoEntity>>{
        return withContext(Dispatchers.IO){
            val simpleVideoByChannelId =
                apiServer.getSimpleVideoByChannelId(channelId, page, pagesize)
            simpleVideoByChannelId
        }
    }
    //视频类型
    suspend fun getSimpleType():ResponseData<List<SimpleTypeEntity>>{
        return withContext(Dispatchers.IO){
            val simpleType = apiServer.getSimpleType()
            simpleType
        }
    }
}