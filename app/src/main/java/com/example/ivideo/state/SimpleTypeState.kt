package com.example.ivideo.state

import com.example.ivideo.entity.SimpleTypeEntity

sealed class SimpleTypeState {
    //成功
    data class Success(var list:List<SimpleTypeEntity>?):SimpleTypeState()
    //错误
    data class Error(var err:Throwable?):SimpleTypeState()
    //失败
    data class Fail(var msg:String?):SimpleTypeState()
    //加载
    object Loading:SimpleTypeState()
}