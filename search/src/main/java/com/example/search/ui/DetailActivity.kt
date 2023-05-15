package com.example.search.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import com.example.search.R
import com.example.search.databinding.ActivityDetailBinding
import com.shuyu.gsyvideoplayer.model.GSYVideoModel

class DetailActivity : AppCompatActivity() {
    lateinit var bind : ActivityDetailBinding
    var urls = arrayListOf<GSYVideoModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)
        urls.add(GSYVideoModel("http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4", "标题1"))
        bind.videoDecoder.setUp("http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4",true,"标题1")
        bind.videoDecoder.titleTextView.visibility = GONE
        bind.videoDecoder.startPlayLogic()

    }
}