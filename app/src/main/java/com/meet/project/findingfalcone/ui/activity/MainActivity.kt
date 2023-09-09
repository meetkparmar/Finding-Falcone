package com.meet.project.findingfalcone.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.meet.project.findingfalcone.network.models.Planet
import com.meet.project.findingfalcone.network.models.Vehicle

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        initData()
        setContent {
            MainScreen(
                viewModel = viewModel,
                onPlanetSelect = ::onPlanetSelect,
                onVehicleSelect = ::onVehicleSelect,
                onNextClick = ::onNextClick,
                onSubmitClick = ::onSubmitClick,
                onPlayAgainClick = ::onPlayAgainClick,
            )
        }
    }

    private fun initData() {
        viewModel.isLoading = true
        viewModel.fetchPlanets(
            onSuccess = {
                if (it.isEmpty()) {
                    Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.planetList.clear()
                    viewModel.planetList.addAll(it)
                    viewModel.planetList.forEach { planet ->
                        planet.setImage()
                    }
                }
            },
            onFailure = {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )

        viewModel.fetchVehicles(
            onSuccess = { it ->
                if (it.isEmpty()) {
                    Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.vehicleList.clear()
                    viewModel.vehicleList.addAll(it)
                    viewModel.vehicleList.forEach { vehicle ->
                        vehicle.setImage()
                    }
                }
                viewModel.isLoading = false
            },
            onFailure = {
                viewModel.isLoading = false
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun onPlanetSelect(planet: Planet) {
        viewModel.selectedPlanet = planet.name.orEmpty()
    }

    private fun onVehicleSelect(vehicle: Vehicle) {
        viewModel.selectedVehicle = vehicle.name.orEmpty()
    }

    private fun onNextClick() {
        viewModel.isLoading = true
        viewModel.planetNames.add(viewModel.selectedPlanet)
        viewModel.vehicleNames.add(viewModel.selectedVehicle)
        var distance = 0
        var speed = 0

        viewModel.planetList.forEach {
            if (it.name == viewModel.selectedPlanet) {
                it.selected = true
                distance = it.distance ?: 0
            }
        }

        viewModel.vehicleList.forEach {
            if (it.name == viewModel.selectedVehicle) {
                it.totalNo = it.totalNo?.minus(1)
                speed = it.speed ?: 1
            }
        }

        viewModel.selectedPlanet = ""
        viewModel.selectedVehicle = ""
        viewModel.page += 1
        viewModel.timeTaken += (distance / speed)
        viewModel.isLoading = false
    }

    private fun onSubmitClick() {
        viewModel.isLoading = true
        viewModel.planetNames.add(viewModel.selectedPlanet)
        viewModel.vehicleNames.add(viewModel.selectedVehicle)
        viewModel.fetchToken(
            onSuccess = {
                viewModel.token = it.token
                viewModel.token?.let {
                    findFalcon()
                }
            },
            onFailure = {
                viewModel.isLoading = false
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun findFalcon() {
        viewModel.findFalcon(
            onSuccess = {
                viewModel.findFalconResponse = it
                viewModel.showResult = true
                viewModel.isLoading = false
            },
            onFailure = {
                viewModel.isLoading = false
                viewModel.showResult = true
                viewModel.showError = true
                Log.v("FindFalcon", it)
            }
        )
    }

    private fun onPlayAgainClick() {
        viewModel.isLoading = true
        viewModel.showResult = false
        viewModel.showError = false

        viewModel.planetList.clear()
        viewModel.vehicleList.clear()
        viewModel.planetNames.clear()
        viewModel.vehicleNames.clear()
        viewModel.selectedPlanet = ""
        viewModel.selectedVehicle = ""
        viewModel.token = null
        viewModel.findFalconResponse = null
        viewModel.page = 1
        viewModel.timeTaken = 0

        initData()
    }
}
