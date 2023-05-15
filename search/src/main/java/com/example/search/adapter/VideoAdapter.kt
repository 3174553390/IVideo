package com.example.zty01.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.search.databinding.ItemVideoBinding
import com.example.zty01.entity.VideoEntity

class VideoAdapter(val context: Context,var data:List<VideoEntity>,val clickListener: ClickListener):RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {

    lateinit var binding: ItemVideoBinding
    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val title=binding.vTitle
        val image=binding.vIv
        val names=binding.name
        val name_image=binding.nIv
        val tx1=binding.tx1
        val tx2=binding.tx2
        val tx3=binding.tx3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding= ItemVideoBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var video = data.get(position)
        holder.title.text = video.title
        holder.names.text = video.name
        holder.tx1.text = video.playnum.toString()
        holder.tx2.text = video.commentnum.toString()
        holder.tx3.text = video.id.toString()
        Glide.with(context).load(video.videomainimag)
            .apply(RequestOptions().transform(CenterCrop(),RoundedCorners(20))).into(holder.image)
        Glide.with(context).load(video.avatar_url)
            .apply(RequestOptions().transform(CenterCrop(),RoundedCorners(90))).into(holder.name_image)
        holder.itemView.setOnClickListener {
            clickListener.onClick(position,video)
        }
//        holder.image.setOnClickListener {
//            clickListener.onClick(position,video)
//        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ClickListener{
        fun onClick(position: Int,entity:VideoEntity)
    }
}