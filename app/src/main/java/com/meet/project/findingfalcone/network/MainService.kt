package com.meet.project.findingfalcone.network

import com.meet.project.findingfalcone.network.models.FindFalconRequest
import com.meet.project.findingfalcone.network.models.FindFalconResponse
import com.meet.project.findingfalcone.network.models.Planet
import com.meet.project.findingfalcone.network.models.TokenResponse
import com.meet.project.findingfalcone.network.models.Vehicle
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MainService {

    @GET("planets")
    suspend fun fetchPlanets(): Response<List<Planet>>

    @GET("vehicles")
    suspend fun fetchVehicles(): Response<List<Vehicle>>

    @POST("token")
    suspend fun fetchToken(): Response<TokenResponse>

    @POST("find")
    suspend fun findFalcon(@Body request: FindFalconRequest): Response<FindFalconResponse>

}