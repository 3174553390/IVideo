package com.example.ivideo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.example.ivideo.adapter.VideoAdapter
import com.example.ivideo.databinding.ActivityMainBinding
import com.example.ivideo.entity.VideoEntity
import com.example.ivideo.state.VideoUiState
import com.example.ivideo.viewmodel.VideoIntent
import com.example.ivideo.viewmodel.VideoVM
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var vm:VideoVM
    lateinit var videoAdapter: VideoAdapter
    lateinit var list: List<VideoEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this).get(VideoVM::class.java)
        //给集合赋空
        list = arrayListOf()
        //适配器初始化
        videoAdapter = VideoAdapter(this,list,object :VideoAdapter.ClickListener{
            override fun onClick(position: Int, entity: VideoEntity) {

            }
        })
        binding.rv.adapter = videoAdapter
        binding.rv.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            vm.uiState.collect{ uiState ->
                when(uiState){
                    is VideoUiState.Loading->{
                        ToastUtils.showShort("加载中.....")
                    }
                    is VideoUiState.Success->{
                        //更新UI
                        updateUI(uiState)
                    }
                }
            }
        }


        lifecycleScope.launch {
            //请求列表意图
            vm.channel.send(VideoIntent.getVideos("94349546935",1,10))

        }
    }

    private fun updateUI(uiState: VideoUiState.Success) {
        if(uiState.list == null){
            return
        }
        videoAdapter.data = uiState.list!!
        videoAdapter.notifyDataSetChanged()
    }
}