package com.example.ivideo

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter

class App:Application() {
    companion object{
        @JvmField
        var context:Context ?= null
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        ARouter.openDebug()
        ARouter.openLog()
        ARouter.init(this) //注意！！！init放在后面
    }
}