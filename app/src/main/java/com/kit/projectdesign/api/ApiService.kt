package com.kit.projectdesign.api

import com.kit.projectdesign.data.DiscountItem
import com.kit.projectdesign.data.Notification
import com.kit.projectdesign.data.Recipe
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("discounts")
    suspend fun getDiscountItems(): Response<List<DiscountItem>>

    @GET("recipes")
    suspend fun getRecipes(): Response<List<Recipe>>

    @GET("notifications")
    suspend fun getNotifications(): Response<List<Notification>>
}
