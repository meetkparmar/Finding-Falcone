package com.meet.project.findingfalcone.network.models

import com.google.gson.annotations.SerializedName

data class FindFalconResponse(
    @SerializedName("planet_name") val planetName: String?,
    val status: String?,
    val error: String?,
)