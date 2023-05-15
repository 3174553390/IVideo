package com.example.ivideo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivideo.reposities.VideoRePo
import com.example.ivideo.state.SimpleTypeState
import com.example.ivideo.state.VideoUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

sealed class SimpleTypeIntent{
    object getSimpleType:SimpleTypeIntent()
    data class getVideo(val channelId:String,val page:Int,val pagesize:Int):SimpleTypeIntent()
}
class SimpleTypeVM:ViewModel() {
    var videoRePo= VideoRePo()
    /**
     * Channel.UNLIMITED设置为最大容量
     * 获得一个channel通道
     * */
    var channel = Channel<SimpleTypeIntent>(Channel.UNLIMITED)
    /**
     * 把VideoUiState意图放到状态流里面
     * MutableStateFlow转换流
     * 针对本类访问
     * */
    private val _uiState = MutableStateFlow<SimpleTypeState>(SimpleTypeState.Loading)
    private val _uiStateVideo = MutableStateFlow<VideoUiState>(VideoUiState.Loading)
    /**
     * 初始化获取状态
     * */
    val uiState : StateFlow<SimpleTypeState>
    get() = _uiState
    val uiStateVideo : StateFlow<VideoUiState>
        get() = _uiStateVideo
    init {
        handlerIntent()
    }
    //处理意图
    private fun handlerIntent() {
        viewModelScope.launch {
            //接收意图根据意图进行操作
            channel.consumeAsFlow().collect(){
                when(it){
                    is SimpleTypeIntent.getSimpleType -> getSimpleType()
                    is SimpleTypeIntent.getVideo -> getVideo(it.channelId,it.page,it.pagesize)
                }
            }
        }
    }
    //数据
    private fun getVideo(channelId: String, page: Int, pagesize: Int) {
        viewModelScope.launch {
            val data =
                videoRePo.getSimpleVideoByChannelId(channelId, page, pagesize)
            if (data.code == 0){
                _uiStateVideo.value = VideoUiState.Success(data.data)
            }else{
                _uiStateVideo.value = VideoUiState.Fail(data.msg)
            }
        }
    }
    //类型
    private fun getSimpleType() {
        viewModelScope.launch {
            val data = videoRePo.getSimpleType()
            if (data.code == 0){
               _uiState.value = SimpleTypeState.Success(data.data)
            }else{
                _uiState.value = SimpleTypeState.Fail(data.msg)
            }
        }
    }
}