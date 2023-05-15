package com.example.ivideo.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ivideo.databinding.ItemSvideoBinding
import com.example.ivideo.databinding.ItemVideoBinding
import com.example.ivideo.entity.VideoEntity
import com.example.ivideo.view.activity.FullScreenActivity
import java.nio.file.attribute.AclEntry

//val clickListener: VideoAdapter.ClickListener
class VideoAdapter(val context: Context?, var data:List<VideoEntity>,val clickListener: ClickListener):RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {
    lateinit var binding: ItemSvideoBinding
    inner class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        var gsy = binding.gsy
        var clfull = binding.clFull
        var zan = binding.ivZan
        var zanNum = binding.tvZan
        var pl = binding.ivPl
        var plNum = binding.tvPl
        var sc = binding.ivSc
        var scNum = binding.tvSc
        var Ivhead = binding.ivAvatarUrl
        var name = binding.tvName
        var title = binding.tvTitle
        var tuijian = binding.tvTuijian
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemSvideoBinding.inflate(LayoutInflater.from(context),parent,false)
        //绑定视图
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var entity = data.get(position)
        holder.title.setText(entity.title)
        holder.name.setText(entity.name)
        holder.zanNum.setText(""+entity.commentnum)
        holder.plNum.setText(""+entity.playnum)
        holder.scNum.setText(""+entity.playnum)
        //Glide.with(context!!).load(entity.avatar_url).apply(RequestOptions().transform(CenterCrop(),RoundedCorners(20))).into(holder.image)
        Glide.with(context!!).load(entity.avatar_url).apply(RequestOptions().transform(CenterCrop(),CircleCrop())).into(holder.Ivhead)
        holder.gsy.setUp(entity.videopath,true,"")
        //设置缩略图
        val imageView = ImageView(context)
        Glide.with(context).load(entity.videomainimag).apply(RequestOptions().transform(CenterCrop())).into(imageView)
        holder.gsy.thumbImageView = imageView
        //设置动画
        holder.gsy.isShowFullAnimation = true
        //全屏就锁屏横屏，默认false竖屏，可配合setRotateViewAuto使用
        holder.gsy.isLockLand = true
        holder.gsy.isRotateViewAuto = true
        //隐藏返回按钮和全屏按钮
        holder.gsy.backButton.visibility = View.GONE
        holder.gsy.fullscreenButton.visibility = View.GONE
        holder.tuijian.setText("搜索 . ${entity.name}")
        //全屏
        holder.clfull.setOnClickListener {
            //holder.gsy.startWindowFullscreen(context,false,false)
            var bundle = Bundle()
            bundle.putInt("id",entity.id)
            bundle.putString("channelid",entity.channelid)
            bundle.putString("videourl",entity.videopath)
            bundle.putInt("index",position)
            var intent = Intent(context,FullScreenActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
        //条目点击
        holder.itemView.setOnClickListener{
            clickListener.onClick(position,entity)
        }
        //点击图片
//        holder.image.setOnClickListener{
//            clickListener.onClick(position,entity)
//        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    interface ClickListener{
        fun onClick(position: Int,entity: VideoEntity)
    }
}