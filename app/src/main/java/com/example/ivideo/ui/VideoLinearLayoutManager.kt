package com.example.ivideo.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class VideoLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {
    private var pagerSnapHelper : PagerSnapHelper ?= null
    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper!!.attachToRecyclerView(view)
    }
}