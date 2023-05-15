package com.example.ivideo.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.ivideo.R
import com.example.ivideo.adapter.FullScreenAdapter
import com.example.ivideo.databinding.ActivityFullScreenBinding
import com.example.ivideo.entity.VideoEntity
import com.example.ivideo.state.FullScreenState
import com.example.ivideo.ui.VideoLinearLayoutManager
import com.example.ivideo.viewmodel.FullScreenIntent
import com.example.ivideo.viewmodel.FullScreenVM
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



@Route(path = "/app/FullScreenActivity")
class FullScreenActivity : AppCompatActivity() {
    lateinit var binding:ActivityFullScreenBinding
    lateinit var list:List<VideoEntity>
    var index = 0
    var id = 0
    lateinit var fullScreenAdapter: FullScreenAdapter
    lateinit var videoLinearLayoutManager: VideoLinearLayoutManager
    lateinit var vm: FullScreenVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ARouter.getInstance().inject(this)
        val bundle = intent.extras
        id = bundle!!.getInt("id")
        index = bundle!!.getInt("index")
        val channelid: String = bundle!!.getString("channelid") as String
        val videourl: String = bundle!!.getString("videourl") as String
        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoLinearLayoutManager  = VideoLinearLayoutManager(this)
        list = arrayListOf()

        vm = ViewModelProvider(this).get(FullScreenVM::class.java)
        fullScreenAdapter = FullScreenAdapter(this,list,videourl,object :FullScreenAdapter.ClickListener{
            override fun onClick(position: Int, entity: VideoEntity) {

            }
        })
        binding.rvFull.adapter = fullScreenAdapter
        binding.rvFull.layoutManager = videoLinearLayoutManager
        binding.rvFull.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val standardGSYVideoPlayer =
                    recyclerView.findViewById<StandardGSYVideoPlayer>(R.id.gsy_home_detail)
                if (standardGSYVideoPlayer != null){
                    standardGSYVideoPlayer.startPlayLogic()
                }
            }
        })


        //发送
        lifecycleScope.launch {
            vm.channel.send(FullScreenIntent.getVideos(channelid,1,10))
        }

        //接收
        lifecycleScope.launch {
            vm.uiState.collect { uiState->
                when (uiState){
                    is FullScreenState.Loading ->{
                        Log.d("===", "加载横屏数据")
                    }
                    is FullScreenState.Success ->{
                        updateUI(uiState)

                    }
                }
            }
        }

    }

    private fun updateUI(uiState: FullScreenState.Success) {
        if (uiState.list == null){
            return
        }
        fullScreenAdapter.data = uiState.list!!
        var iq = 0
        for (i in fullScreenAdapter.data){
            if (i.id == id){
                binding.rvFull.scrollToPosition(iq)
            }
            iq++
        }
        fullScreenAdapter.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }
}