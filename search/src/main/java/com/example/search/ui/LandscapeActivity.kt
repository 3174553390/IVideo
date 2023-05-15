package com.example.search.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.search.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class LandscapeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landscape)
        var a: String
        var b: String
        GlobalScope.launch{
            withContext(Dispatchers.IO) {
                Thread.sleep(10000)

            }
        }
        Log.d("===","ssss")
    }
}