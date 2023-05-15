package com.example.ivideo.state

import com.example.ivideo.entity.VideoEntity

sealed class FullScreenState {
     data class Success(var list: List<VideoEntity>?):FullScreenState()
     data class Error(var err:Throwable?):FullScreenState()
     data class Fail(var msg:String?):FullScreenState()
     object Loading:FullScreenState()
}