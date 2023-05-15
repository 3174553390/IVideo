package com.example.mymvi.state

import com.example.zty01.entity.TypeEntity
import com.example.zty01.entity.VideoEntity


sealed class VideoUiState {
    data class TypeSuccess(var list: List<TypeEntity>?):VideoUiState()
    data class Success(var list: List<VideoEntity>?):VideoUiState()

    data class Error(var ex:Throwable?):VideoUiState()
    data class Fail(var msg:String?):VideoUiState()
    object Loading:VideoUiState()



}