package com.example.zty.http

import android.text.TextUtils
import com.example.search.App
import com.example.search.utlis.ACache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object RetrofitManager {
    var mApi : ApiSevice? = null

    fun getApiService() :ApiSevice{
        if (mApi == null){
            synchronized(this){
                if (mApi == null){
                    val okHttpClient =
                        buildOkHttpClient()
                    mApi =
                        buildRetrofit(
                            "http://124.70.98.255:8083/",
                            okHttpClient
                        ).create(ApiSevice::class.java)
                }
            }
        }
        return mApi!!
    }

    private fun buildOkHttpClient(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val builder = chain.request().newBuilder()
                    val token = ACache.get(App.context).getAsString("token")
                    if(!TextUtils.isEmpty(token)){
                        builder.addHeader("sn-token",token)
                    }
                    val request = builder.build()
                    return chain.proceed(request)
                }
            })
            .connectTimeout(6000,TimeUnit.SECONDS)
            .writeTimeout(6000,TimeUnit.SECONDS)
            .readTimeout(6000,TimeUnit.SECONDS)
    }
    private fun buildRetrofit(baseUrl: String, builder: OkHttpClient.Builder): Retrofit {
        val client = builder.build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }


}