package com.example.ivideo.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.MenuItem
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ivideo.R
import com.example.ivideo.adapter.MyFragmentAdapter
import com.example.ivideo.databinding.ActivityBaseBinding
import com.example.ivideo.databinding.ActivityMainBinding
import com.example.ivideo.view.fragment.*
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationBarView

class BaseActivity : AppCompatActivity() {
    lateinit var binding: ActivityBaseBinding
    var list:MutableList<Fragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list.add(HomeFragment())
        list.add(VideoTVFragment())
        list.add(AddFragment())
        list.add(MessageFragment())
        list.add(MeFragment())
        var myFragmentAdapter = MyFragmentAdapter(supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,list)
        binding.vp.adapter = myFragmentAdapter
        binding.vp.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0){
                    binding.bottom.menu.getItem(position).setChecked(true)
                }else if (position == 1){
                    binding.bottom.menu.getItem(position).setChecked(true)
                }else if (position == 2){
                    binding.bottom.menu.getItem(position).setChecked(true)
                }else if (position == 3){
                    binding.bottom.menu.getItem(position).setChecked(true)
                }else if (position == 4){
                    binding.bottom.menu.getItem(position).setChecked(true)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        //取消默认颜色
        //setItemIconTintList(null)
        binding.bottom.itemIconTintList = null
        binding.bottom.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.bottom_home -> binding.vp.currentItem = 0
                    R.id.bottom_videoTV -> binding.vp.currentItem = 1
                    R.id.bottom_add -> binding.vp.currentItem = 2
                    R.id.bottom_message -> binding.vp.currentItem = 3
                    R.id.bottom_me -> binding.vp.currentItem = 4
                }
                return true
            }
        })
    }
}