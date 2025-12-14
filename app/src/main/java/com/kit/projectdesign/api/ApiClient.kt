package com.kit.projectdesign.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // 生产环境 URL
    private const val BASE_URL = "http://pd.yagu1125.com/" 
    
    // 开发环境 (本地测试) 请使用: "http://10.0.2.2:3031/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
