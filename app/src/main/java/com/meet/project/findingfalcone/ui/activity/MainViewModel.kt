package com.meet.project.findingfalcone.ui.activity

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.meet.project.findingfalcone.network.MainService
import com.meet.project.findingfalcone.network.RetrofitClient
import com.meet.project.findingfalcone.network.models.FindFalconRequest
import com.meet.project.findingfalcone.network.models.FindFalconResponse
import com.meet.project.findingfalcone.network.models.Planet
import com.meet.project.findingfalcone.network.models.TokenResponse
import com.meet.project.findingfalcone.network.models.Vehicle
import kotlinx.coroutines.launch

class MainViewModel(context: Application) : AndroidViewModel(context) {

    private val retrofit = RetrofitClient.getInstance()
    private val apiInterface = retrofit.create(MainService::class.java)

    var planetList = mutableStateListOf<Planet>()
    var vehicleList = mutableStateListOf<Vehicle>()
    var planetNames = mutableStateListOf<String>()
    var vehicleNames = mutableStateListOf<String>()
    var selectedPlanet by mutableStateOf(value = "")
    var selectedVehicle by mutableStateOf(value = "")
    var token by mutableStateOf<String?>(value = null)
    var isLoading by mutableStateOf(value = false)
    var showResult by mutableStateOf(value = false)
    var showError by mutableStateOf(value = false)
    var findFalconResponse by mutableStateOf<FindFalconResponse?>(value = null)
    var page by mutableStateOf(value = 1)
    var timeTaken by mutableStateOf(value = 0)


    fun fetchPlanets(
        onSuccess: (List<Planet>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = apiInterface.fetchPlanets()
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    onFailure(response.message() ?: "Something Went Wrong!")
                }
            } catch (e: Exception) {
                onFailure("Something Went Wrong!")
            }
        }
    }

    fun fetchVehicles(
        onSuccess: (List<Vehicle>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = apiInterface.fetchVehicles()
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    onFailure(response.message() ?: "Something Went Wrong!")
                }
            } catch (e: Exception) {
                onFailure("Something Went Wrong!")
            }
        }
    }

    fun fetchToken(
        onSuccess: (TokenResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = apiInterface.fetchToken()
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    onFailure(response.message() ?: "Something Went Wrong!")
                }
            } catch (e: Exception) {
                onFailure("Something Went Wrong!")
            }
        }
    }

    fun findFalcon(
        onSuccess: (FindFalconResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = apiInterface.findFalcon(
                    request = FindFalconRequest(
                        token = token.orEmpty(),
                        planetNames = planetNames,
                        vehicleNames = vehicleNames
                    )
                )
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    onFailure(response.message() ?: "Something Went Wrong!")
                }
            } catch (e: Exception) {
                onFailure("Something Went Wrong!")
            }
        }
    }
}