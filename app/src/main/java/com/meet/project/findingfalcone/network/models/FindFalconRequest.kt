package com.meet.project.findingfalcone.network.models

import com.google.gson.annotations.SerializedName

data class FindFalconRequest(
    var token: String,
    @SerializedName("planet_names") var planetNames: List<String>,
    @SerializedName("vehicle_names") var vehicleNames: List<String>,
)