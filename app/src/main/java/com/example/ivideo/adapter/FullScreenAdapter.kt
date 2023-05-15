package com.example.ivideo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.ivideo.databinding.ItemFullBinding
import com.example.ivideo.db.DBInstant
import com.example.ivideo.db.DanMu
import com.example.ivideo.entity.VideoEntity
import top.littlefogcat.danmakulib.danmaku.Danmaku
import top.littlefogcat.danmakulib.danmaku.DanmakuManager


class FullScreenAdapter(val context: Context,var data:List<VideoEntity>,var videourl:String,val clickListener: ClickListener):
    RecyclerView.Adapter<FullScreenAdapter.MyViewHolder>() {
    lateinit var binding:ItemFullBinding
    var a=0
    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        var gsy = binding.gsyHomeDetail
        var back = binding.ivHomeDetailBack
        var title = binding.tvDetailTitle
        var danmu = binding.danmu
        var oo = binding.oo
        var send = binding.send
        var eddanmu = binding.edDanmu
        var danmuView = binding.dvHomeDetail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemFullBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entity = data.get(position)
        holder.back.setOnClickListener{
            clickListener.onClick(position,entity)
        }
        var imageView = ImageView(context)
        Glide.with(context).load(entity.videomainimag).apply(RequestOptions().transform(CenterCrop())).into(imageView)
        holder.gsy.thumbImageView = imageView
        holder.gsy.setUp(entity.videopath,true,"")
        holder.gsy.isShowFullAnimation = true
        holder.gsy.isLockLand = true
        holder.gsy.isRotateViewAuto = true
        //隐藏返回按钮和全屏按钮
        holder.gsy.backButton.visibility = View.GONE
        holder.gsy.fullscreenButton.visibility = View.GONE
        holder.title.setText(entity.title)
        holder.gsy.startPlayLogic()
        holder.danmu.setOnClickListener{
            holder.oo.visibility = VISIBLE
            holder.eddanmu.requestFocus()//自动聚焦
        }
        holder.send.setOnClickListener{
            holder.oo.visibility = GONE
            val string = holder.eddanmu.text.toString()
            if (string != null){
                var danMu = DanMu(string)
                DBInstant.getAppDatabase().danMuDao().insertDanMu(danMu)
               // val danmakuContext = DanmakuContext()
                DanmakuManager.getInstance().init(context,holder.danmuView)
                val danmaku = Danmaku()
                danmaku.color = "#00ff00"
                danmaku.text = string
                danmaku.size = 100
                DanmakuManager.getInstance().send(danmaku)
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    interface ClickListener{
        fun onClick(position: Int,entity: VideoEntity)
    }
}