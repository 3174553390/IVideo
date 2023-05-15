package com.example.ivideo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivideo.reposities.VideoRePo
import com.example.ivideo.state.FullScreenState
import com.example.ivideo.state.VideoUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

sealed class FullScreenIntent{
    data class getVideos(val channelId:String,val page:Int,val pagesize:Int):FullScreenIntent()
}

class FullScreenVM:ViewModel() {
    var videoRePo = VideoRePo()
    var channel = Channel<FullScreenIntent>(Channel.UNLIMITED)
    private val _uiState = MutableStateFlow<FullScreenState>(FullScreenState.Loading)
    val uiState : StateFlow<FullScreenState>
    get() = _uiState
    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect {
                when(it){
                    is FullScreenIntent.getVideos -> getVideos(it.channelId,it.page,it.pagesize)
                }
            }
        }
    }

    private fun getVideos(channelId: String, page: Int, pagesize: Int) {
        viewModelScope.launch {
            val data =
                videoRePo.getSimpleVideoByChannelId(channelId, page, pagesize)
            if (data.code == 0){
                _uiState.value = FullScreenState.Success(data.data)
            }else{
                _uiState.value = FullScreenState.Fail(data.msg)
            }
        }
    }

}