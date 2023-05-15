package com.example.zty.utlis

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object GlideUtils {
    @JvmStatic
    @BindingAdapter("android:url")
    fun setImage(iv:ImageView,url:String){
        Glide.with(iv).load(url).into(iv)
    }
}