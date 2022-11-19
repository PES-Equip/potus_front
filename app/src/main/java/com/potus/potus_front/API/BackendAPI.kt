package com.potus.potus_front.API

import com.potus.potus_front.API.requests.ActionRequest
import com.potus.potus_front.API.requests.CreateGardenRequest
import com.potus.potus_front.API.requests.InformLocationRequest
import com.potus.potus_front.API.requests.RegisterUserRequest
import com.potus.potus_front.API.response.GardenListResponse
import com.potus.potus_front.API.response.NewGardenResponse
import com.potus.potus_front.API.response.PotusResponse
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

    @GET
    suspend fun getGardenList(@Header("Authorization") token:String, @Url url:String): Response<GardenListResponse>

    @POST
    suspend fun registerUser(@Header("Authorization") token:String, @Url url:String, @Body requestModel: RegisterUserRequest): Response<UserResponse>

    @POST
    suspend fun actions(@Header("Authorization") token:String, @Url url:String, @Body requestModel: ActionRequest): Response<UserResponse>

    @POST
    suspend fun informLocation(@Header("Authorization") token:String, @Url url:String, @Body requestModel: InformLocationRequest): Response<PotusResponse>

    @POST
    suspend fun createGarden(@Header("Authorization") token:String, @Url url:String, @Body requestModel: CreateGardenRequest): Response<NewGardenResponse>
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        //.baseUrl("https://potusback-production.up.railway.app/api/")
        .baseUrl("http://10.0.2.2:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
