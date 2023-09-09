package com.meet.project.findingfalcone.network.models

import com.google.gson.annotations.SerializedName
import com.meet.project.findingfalcone.R

data class Vehicle(
    var name: String?,
    @SerializedName("total_no") var totalNo: Int? = 0,
    @SerializedName("max_distance") var maxDistance: Int? = 0,
    var speed: Int? = 1,
    var image: Int?,
) {
    fun setImage() {
        image = when(name?.toLowerCase()) {
            "space pod" -> R.drawable.space_pod
            "space rocket" -> R.drawable.space_rocket
            "space ship" -> R.drawable.space_ship
            "space shuttle" -> R.drawable.space_shuttle
            else -> R.drawable.rocket
        }
    }
}