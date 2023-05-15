package com.example.search.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.search.databinding.ItemHistoryBinding
import com.example.search.room.HistoryBean

class HistoryAdapter(layoutResId: Int) : BaseQuickAdapter<HistoryBean, BaseViewHolder>(layoutResId) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        DataBindingUtil.bind<ViewDataBinding>(holder.itemView)
        super.onBindViewHolder(holder, position)
    }

    override fun convert(helper: BaseViewHolder, item: HistoryBean) {
        var binding = DataBindingUtil.getBinding<ItemHistoryBinding>(helper.itemView)
        binding?.history = item
    }
}