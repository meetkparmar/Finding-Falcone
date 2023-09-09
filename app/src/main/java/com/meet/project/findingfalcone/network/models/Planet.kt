package com.meet.project.findingfalcone.network.models

import com.meet.project.findingfalcone.R

data class Planet(
    val name: String?,
    val distance: Int? = 0,
    var image: Int?,
    var selected: Boolean = false
) {
    fun setImage() {
        image = when(name?.toLowerCase()) {
            "donlon" -> R.drawable.donlon
            "enchai" -> R.drawable.enchai
            "jebing" -> R.drawable.jebing
            "lerbin" -> R.drawable.lerbin
            "pingasor" -> R.drawable.pingasor
            "sapir" -> R.drawable.sapir
            else -> R.drawable.planet
        }
    }
}