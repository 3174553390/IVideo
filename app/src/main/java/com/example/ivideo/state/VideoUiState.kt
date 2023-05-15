package com.example.ivideo.state

import com.example.ivideo.entity.VideoEntity
import kotlinx.coroutines.flow.MutableStateFlow

sealed class VideoUiState {
    data class Success(var list: List<VideoEntity>?):VideoUiState()
    data class Error(var err:Throwable?):VideoUiState()
    data class Fail(var msg:String?):VideoUiState()
    object Loading:VideoUiState()
}