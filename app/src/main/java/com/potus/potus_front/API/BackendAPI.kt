package com.potus.potus_front.API

import com.potus.potus_front.API.requests.*
import com.potus.potus_front.API.response.*
import com.potus.potus_front.API.response.data_models.SimplifiedGardenMember
import com.potus.potus_front.API.response.data_models.UserGardenInfo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIService {

    @GET
    suspend fun getUser(@Header("Authorization") token:String, @Url url:String): Response<UserResponse>

    @GET
    suspend fun getGarden(@Header("Authorization") token:String, @Url url:String, @Query("garden") garden:String): Response<GardenResponse>

    @GET
    suspend fun getGardenList(@Header("Authorization") token:String, @Url url:String): Response<List<NewGardenResponse>>

    @GET
    suspend fun getGardenMembers(@Header("Authorization") token:String, @Url url:String): Response<Set<SimplifiedGardenMember>>

    @GET
    suspend fun getInvitationList(@Header("Authorization") token:String, @Url url:String): Response<List<NewGardenResponse>>

    @GET
    suspend fun getGardenPetitionList(@Header("Authorization") token:String, @Url url:String, @Query("garden") garden:String): Response<List<GardenMemberResponse>>

    @GET
    suspend fun getGases(@Header("Authorization") token:String, @Url url:String, @Query("latitude") latitude : Double, @Query("length") length : Double): Response<GasesResponse>

    @GET
    suspend fun getHistory(@Header("Authorization") token:String, @Url url:String): Response<List<HistoryResponse>>

    @POST
    suspend fun registerUser(@Header("Authorization") token:String, @Url url:String, @Body requestModel: RegisterUserRequest): Response<UserResponse>

    @POST
    suspend fun actions(@Header("Authorization") token:String, @Url url:String, @Body requestModel: ActionRequest): Response<UserResponse>

    @POST
    suspend fun changeUsername(@Header("Authorization") token:String, @Url url:String, @Body requestModel: ChangeUsernameRequest): Response<UserResponse>

    @DELETE
    suspend fun deleteAccount(@Header("Authorization") token:String, @Url url:String): Response<UserResponse>

    @POST
    suspend fun informLocation(@Header("Authorization") token:String, @Url url:String, @Body requestModel: InformLocationRequest): Response<PotusResponse>

    @POST
    suspend fun revivePotus(@Header("Authorization") token:String, @Url url:String, @Body requestModel: PotusReviveRequest): Response<PotusResponse>

    @POST
    suspend fun createGarden(@Header("Authorization") token:String, @Url url:String, @Body requestModel: GardenRequest): Response<UserGardenInfo>

    @POST
    suspend fun askToJoinGarden(@Header("Authorization") token:String, @Url url:String, @Query("garden") garden:String)

    @POST
    suspend fun sendGardenInvitation(@Header("Authorization") token:String, @Url url:String, @Query("garden") garden:String, @Query("user") user:String)

    @PUT
    suspend fun joinGarden(@Header("Authorization") token:String, @Url url:String, @Query("garden") garden:String): Response<GardenResponse>

    @PUT
    suspend fun changeGardenDescription(@Header("Authorization") token:String, @Url url:String, @Body requestModel: GardenDescriptionRequest): Response<NewGardenResponse>

    @POST
    suspend fun acceptGardenPetition(@Header("Authorization") token:String, @Url url:String, @Body requestModel: GardenInvitationRequest): Response<List<GardenMemberResponse>>

    @DELETE
    suspend fun refuseToJoinGarden(@Header("Authorization") token:String, @Url url:String, @Query("garden") garden:String)

    @DELETE
    suspend fun refuseGardenPetition(@Header("Authorization") token:String, @Url url:String, @Query("garden") garden:String, @Query("user") user:String)

    @DELETE
    suspend fun exitGarden(@Header("Authorization") token:String, @Url url:String)

    @DELETE
    suspend fun removeGarden(@Header("Authorization") token:String, @Url url:String, @Query("garden") garden:String)
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        //.baseUrl("https://potusback-production-b295.up.railway.app/api/")
        .baseUrl("http://10.0.2.2:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
