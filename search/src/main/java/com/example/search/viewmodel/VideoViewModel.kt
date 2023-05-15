package com.example.zty01.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymvi.state.VideoUiState
import com.example.zty01.repository.VideoRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch


sealed class VideoIntent{
    data class getVideos(val channelId:String,val page:Int,val pagesize:Int):VideoIntent()
    data class getType(val a :String) :VideoIntent()
    object getDetail:VideoIntent()
}
class VideoViewMdel:ViewModel(){
    var videoRepo= VideoRepo()
    val channel= Channel<VideoIntent>(Channel.UNLIMITED)
    private val _uiState= MutableStateFlow<VideoUiState>(VideoUiState.Loading)
    val uiState: StateFlow<VideoUiState>get() = _uiState

    init {
        handlerIntent()
    }
    private fun handlerIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect {
                when(it){
                    is VideoIntent.getVideos -> getVideos(it.channelId,it.page,it.pagesize)
                    is VideoIntent.getType -> getType()
                }
            }
        }
    }

    private fun getVideos(channelId:String,page:Int,pagesize:Int) {
        viewModelScope.launch {
            val data = videoRepo.getSimpleVideoByChannelId(channelId, page, pagesize)
            if(data.code==0){
                _uiState.value= VideoUiState.Success(data.data)
            }else{
                _uiState.value= VideoUiState.Fail(data.msg)
            }
        }
    }
    private fun getType() {
        viewModelScope.launch {
            val data = videoRepo.getSimpleType()
            if(data.code==0){
                _uiState.value= VideoUiState.TypeSuccess(data.data)
            }else{
                _uiState.value= VideoUiState.Fail(data.msg)
            }
        }
    }
}