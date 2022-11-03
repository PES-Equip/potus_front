package com.potus.potus_front.API

import com.potus.potus_front.API.requests.ActionRequest
import com.potus.potus_front.API.requests.RegisterUserRequest
import com.potus.potus_front.API.response.UserResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {

    @GET
    suspend fun getUser(@Header("Authorization") token:String, @Url url:String): Response<UserResponse>

    @POST
    suspend fun registerUser(@Header("Authorization") token:String, @Url url:String, @Body requestModel: RegisterUserRequest): Response<UserResponse>

    @POST
    suspend fun actions(@Header("Authorization") token:String, @Url url:String, @Body requestModel: ActionRequest): Response<UserResponse>
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/")
        //.baseUrl("https://potusback-production.up.railway.app/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
