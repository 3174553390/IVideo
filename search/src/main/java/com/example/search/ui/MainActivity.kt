package com.example.search.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.mymvi.state.VideoUiState
import com.example.search.R
import com.example.search.adapter.HistoryAdapter
import com.example.search.databinding.ActivityMainBinding
import com.example.search.room.AppHistorybase
import com.example.search.room.HistoryBean
import com.example.zty01.adapter.VideoAdapter
import com.example.zty01.entity.VideoEntity
import com.example.zty01.viewmodel.VideoIntent
import com.example.zty01.viewmodel.VideoViewMdel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val historyDao=AppHistorybase.getInstance(this).historydao()
    lateinit var bind : ActivityMainBinding
    lateinit var vm : VideoViewMdel
    lateinit var videoAdapter: VideoAdapter
    lateinit var historyAdapter : HistoryAdapter
    lateinit var list : List<VideoEntity>
    var lists = arrayListOf<VideoEntity>()
    var listz = arrayListOf<VideoEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        vm = ViewModelProvider(this).get(VideoViewMdel::class.java)
        historyAdapter = HistoryAdapter(R.layout.item_history)
        lifecycleScope.launch {
            vm.channel.send(VideoIntent.getType(""))
        }
        bind.back.setOnClickListener{
            finish()
        }
        bind.rvSearchRoom.layoutManager = StaggeredGridLayoutManager(5, SCROLL_AXIS_HORIZONTAL)
        bind.rvSearchRoom.adapter = historyAdapter
        historyAdapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                            Thread{
                                kotlin.run {
                                    historyDao.deleteByTom(historyAdapter.data.get(position).title)
                                    historyAdapter.data.clear()
                                    historyAdapter.data= historyDao.getAllHistory() as ArrayList<HistoryBean>
                                    GlobalScope.launch(Dispatchers.Main) {
                                       historyAdapter.notifyDataSetChanged()
                                    }
                                }
                            }.start()
                    }
        })

      Thread{
          kotlin.run {
              historyAdapter.data = historyDao.getAllHistory() as ArrayList<HistoryBean>
              historyAdapter.notifyDataSetChanged()
          }
        }.start()
        bind.sea.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                Thread{
                    kotlin.run {
                        val historyBean = HistoryBean(text)
                        historyDao.insert(historyBean)
                    }
                }.start()
                bind.u1.visibility = GONE
                bind.rv.visibility = VISIBLE
                listz.clear()
                for (i in lists){
                    if (i.title.contains(text)){
                        listz.add(i)
                        Log.d("z===",i.toString())
                    }
                }
                videoAdapter.data = listz
                videoAdapter.notifyDataSetChanged()
                bind.sea.isIconified = true
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        list = arrayListOf()
        lists = arrayListOf()
        videoAdapter = VideoAdapter(this,list,object :VideoAdapter.ClickListener{
            override fun onClick(position: Int, entity: VideoEntity) {

            }
        })
        bind.rv.adapter = videoAdapter
        bind.rv.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            vm.uiState.collect{ uiState->
                when(uiState){
                    is VideoUiState.Loading->{
                        Log.i("===","加载中……")
                    }
                    is VideoUiState.TypeSuccess->{
                        getdata(uiState)
                    }
                    is VideoUiState.Success->{
                        updateUI(uiState)
                    }
                }
            }
        }

    }
    private fun updateUI(uiState: VideoUiState.Success) {
        if(uiState.list==null){
            return
        }
        Log.i("===",uiState.list!!.toString())
        lists.addAll(uiState.list!!)
    }
    private suspend fun getdata(uiState: VideoUiState.TypeSuccess) {
        if(uiState.list==null){
            return
        }
        for (t in uiState.list!!){
            for (i in 1 until 35) {
//                if(i == 30 ||  i == 36|| i == 37|| i == 42|| i == 28|| i == 38|| i == 40|| i == 46|| i == 39
//                    || i == 49|| i == 47|| i == 45|| i == 41|| i == 35|| i == 24|| i == 5|| i == 23|| i == 44
//                    || i == 48|| i == 2|| i == 9|| i == 31|| i == 12|| i == 27|| i == 32|| i == 8|| i == 22
//                    || i == 4|| i == 10|| i == 1|| i == 15|| i == 17|| i == 26|| i == 13|| i == 6|| i == 18
//                    || i == 43|| i == 7|| i == 11|| i == 21|| i == 3|| i == 16|| i == 20|| i == 19|| i == 14
//                    || i == 25|| i == 34|| i == 29|| i == 33){
                if (t.channelid.equals("2147483647")!=true){
                    Log.d("===",t.channelid)
                    vm.channel.send(VideoIntent.getVideos(t.channelid,i,100))
                }
//                }
            }
        }

    }
}