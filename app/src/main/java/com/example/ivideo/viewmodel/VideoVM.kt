package com.example.ivideo.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivideo.reposities.VideoRePo
import com.example.ivideo.state.VideoUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch


/**
 * sealed密封类
 * VideoIntent意图
 * */
sealed class VideoIntent{
    //携带参数
    data class getVideos(val channelId:String,val page:Int,val pagesize:Int):VideoIntent()
    //不携带参数
    //object getDetail:VideoIntent()
}

class VideoVM : ViewModel(){
    var videoRePo=VideoRePo()
    /**
     * Channel.UNLIMITED 最大容量
     * */
    //获取一个channel通道
    val channel = Channel<VideoIntent>(Channel.UNLIMITED)
    /**
     * 把VideoUiState意图放到状态流里面
     * MutableStateFlow转换流
     * */
    //针对本类访问
    private val _uiState = MutableStateFlow<VideoUiState>(VideoUiState.Loading)
    //初始化获取状态
    val uiState: StateFlow<VideoUiState>
    get() = _uiState
    init {
        handlerIntent()
    }
    //处理意图
    private fun handlerIntent() {
     viewModelScope.launch {
       //接收到意图，根据意图做操作
         channel.consumeAsFlow().collect{
             when(it){
                 is VideoIntent.getVideos -> getVideos(it.channelId,it.page,it.pagesize)
             }
         }
       }
    }

    private fun getVideos(channelId: String, page: Int, pagesize: Int) {
       viewModelScope.launch {
           val data =
               videoRePo.getSimpleVideoByChannelId(channelId, page, pagesize)
           if (data.code == 0){
               //成功
               _uiState.value = VideoUiState.Success(data.data)
           }else{
               //失败
               _uiState.value = VideoUiState.Fail(data.msg)
           }
       }
    }
}