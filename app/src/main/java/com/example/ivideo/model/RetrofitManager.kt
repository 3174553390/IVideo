package com.example.ivideo.model

import android.text.TextUtils
import com.example.ivideo.App
import com.example.ivideo.utils.ACache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    var mApi:ApiServer ?= null
    fun getApiServer():ApiServer{
        if (mApi == null){
            synchronized(this){
                if (mApi == null){
                    val okHttpClient = createOkHttpClient()
                    mApi = createRetrofit("http://124.70.98.255:8083/",okHttpClient)
                        .create(ApiServer::class.java)
                }
            }
        }
        return mApi!!
    }

    private fun createRetrofit(baseUrl:String,builder:OkHttpClient.Builder):Retrofit {
        val client = builder.build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
    }

    private fun createOkHttpClient():OkHttpClient.Builder {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(object :Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val builder = chain.request().newBuilder()
  //                  val token = ACache.get(App.context).getAsString("token")
//                    if (!TextUtils.isEmpty(token)){
//                        builder.addHeader("sn-token",token)
//                    }
                    val build = builder.build()
                    return chain.proceed(build)
                }
            })
            .readTimeout(5,TimeUnit.SECONDS)
            .writeTimeout(5,TimeUnit.SECONDS)
            .connectTimeout(5,TimeUnit.SECONDS)
    }

}