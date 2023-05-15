package com.example.ivideo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ivideo.R
import com.example.ivideo.databinding.FragmentHomeBinding
import com.example.ivideo.databinding.ItemSimpletypeBinding
import com.example.ivideo.entity.SimpleTypeEntity

class SimpleTypeAdapter(val context: Context?, var data:List<SimpleTypeEntity>, val clickListener: ClickListener):
    RecyclerView.Adapter<SimpleTypeAdapter.myViewHolder>() {
    lateinit var binding: ItemSimpletypeBinding
    inner class myViewHolder(view: View):RecyclerView.ViewHolder(view){
        var type = binding.tvSimletype
    }
    //初始化
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        binding = ItemSimpletypeBinding.inflate(LayoutInflater.from(context),parent,false)
        return myViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
            var simpleTypeEntity = data.get(position)
//            if (position == 0){
//                holder.type.setTextColor(context!!.resources.getColor(R.color.white))
//            }
            holder.type.setText(simpleTypeEntity.typename)
            holder.itemView.setOnClickListener{
                clickListener.onClick(position,simpleTypeEntity)
            }
    //    }


    }

    override fun getItemCount(): Int {
        return data.size
    }
    interface ClickListener{
        fun onClick(position: Int,entity: SimpleTypeEntity)
    }
}