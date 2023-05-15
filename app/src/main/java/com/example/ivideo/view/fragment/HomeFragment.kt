package com.example.ivideo.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ivideo.R
import com.example.ivideo.adapter.SimpleTypeAdapter
import com.example.ivideo.adapter.VideoAdapter
import com.example.ivideo.databinding.FragmentHomeBinding
import com.example.ivideo.entity.SimpleTypeEntity
import com.example.ivideo.entity.VideoEntity
import com.example.ivideo.state.SimpleTypeState
import com.example.ivideo.state.VideoUiState
import com.example.ivideo.ui.VideoLinearLayoutManager
import com.example.ivideo.viewmodel.SimpleTypeIntent
import com.example.ivideo.viewmodel.SimpleTypeVM
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding
    lateinit var vm:SimpleTypeVM
    lateinit var list:List<SimpleTypeEntity>
    lateinit var videos:List<VideoEntity>
    lateinit var simpleTypeAdapter:SimpleTypeAdapter
    lateinit var videoAdapter: VideoAdapter
    lateinit var videoLinearLayoutManager: VideoLinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        vm = ViewModelProvider(this).get(SimpleTypeVM::class.java)
        //集合赋值为空
        list = arrayListOf()
        videos = arrayListOf()
        //初始化RecyclerView
        binding.tab.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        //初始化自定义LinearLayoutManager
        videoLinearLayoutManager = VideoLinearLayoutManager(context)
        binding.rvSimpleVideo.layoutManager = videoLinearLayoutManager
        videoAdapter = VideoAdapter(context,videos,object :VideoAdapter.ClickListener{
            override fun onClick(position: Int, entity: VideoEntity) {

            }
        })
        binding.rvSimpleVideo.adapter = videoAdapter
        //根据channelid实现内容切换
        simpleTypeAdapter = SimpleTypeAdapter(context,list,object :SimpleTypeAdapter.ClickListener{
            override fun onClick(position: Int, entity: SimpleTypeEntity) {
                val channelid = entity.channelid
                getVideo(channelid)
                simpleTypeAdapter.notifyDataSetChanged()
            }
        })
        binding.tab.adapter = simpleTypeAdapter
        /**
         * 滑动播放
         * */
        binding.rvSimpleVideo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //获取当前播放的下标
//                val position =
//                    videoLinearLayoutManager.findFirstVisibleItemPosition()
                //获取播放器
                val gsy = recyclerView.findViewById<StandardGSYVideoPlayer>(R.id.gsy)
                if (gsy != null){
                    //播放
                    gsy.startPlayLogic()
                }
            }
        })

        
        //接收视频
        lifecycleScope.launch {
            vm.uiStateVideo.collect { uiStateVideo ->
                when(uiStateVideo){
                    is VideoUiState.Loading ->{
                        Log.d("===", "uiStateVideo: 加载中.....")
                    }
                    is VideoUiState.Success ->{
                        updataVideoUI(uiStateVideo)
                    }
                }
            }
        }



        //接收类型
        lifecycleScope.launch {
            vm.uiState.collect { uiState ->
                when(uiState){
                    is SimpleTypeState.Loading ->{
                        Log.d("===", "SimpleTypeState: 加载中.....")
                    }
                    is SimpleTypeState.Success ->{
                        updateUi(uiState)
                    }
                }
            }
        }
        //发送意图
        lifecycleScope.launch {
            vm.channel.send(SimpleTypeIntent.getSimpleType)
            //vm.channel.send(SimpleTypeIntent.getVideo("2147483647",1,10))
        }
        return binding.root
    }
    //视频
    private fun updataVideoUI(uiStateVideo: VideoUiState.Success) {
        if (uiStateVideo.list == null){
            return
        }
        videoAdapter.data = uiStateVideo.list!!
        videoAdapter.notifyDataSetChanged()
    }

    private fun getVideo(id:String){
       lifecycleScope.launch {
           vm.channel.send(SimpleTypeIntent.getVideo(id,1,10))
       }
    }
    //类型
    private fun updateUi(uiState: SimpleTypeState.Success) {
        if (uiState.list == null){
            return
        }
        //默认加载第一项
        uiState.list!!.get(0).flag = true
        getVideo(uiState.list!!.get(0).channelid)
        simpleTypeAdapter.data = uiState.list!!
        simpleTypeAdapter.notifyDataSetChanged()
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