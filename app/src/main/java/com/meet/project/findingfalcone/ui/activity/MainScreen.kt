package com.meet.project.findingfalcone.ui.activity

import ThemeTypography
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.meet.project.findingfalcone.R
import com.meet.project.findingfalcone.network.models.Planet
import com.meet.project.findingfalcone.network.models.Vehicle
import com.meet.project.findingfalcone.ui.theme.ThemeColor

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onPlanetSelect: (Planet) -> Unit,
    onVehicleSelect: (Vehicle) -> Unit,
    onNextClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onPlayAgainClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ThemeColor.Primary
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = stringResource(id = R.string.app_name),
                style = ThemeTypography.h4,
                color = ThemeColor.Secondary,
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = ThemeColor.Primary
                )
            } else if (viewModel.showResult) {
                ResultPage(
                    viewModel = viewModel,
                    onPlayAgainClick = onPlayAgainClick
                )
            } else {
                LazyColumn() {
                    item(key = "selection") {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            text = "Selection: ${viewModel.page} out of 4",
                            style = ThemeTypography.subtitle1,
                            color = ThemeColor.Primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item(key = "planetSection") {
                        PlanetSection(viewModel = viewModel, onClick = onPlanetSelect)
                    }
                    item(key = "vehicleSection") {
                        VehicleSection(viewModel = viewModel, onClick = onVehicleSelect)
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .align(Alignment.BottomCenter),
                    enabled = viewModel.selectedPlanet.isNotEmpty() && viewModel.selectedVehicle.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = { if (viewModel.page == 4) onSubmitClick() else onNextClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ThemeColor.Primary,
                        contentColor = ThemeColor.Grey20
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = stringResource(id = if (viewModel.page == 4) R.string.submit else R.string.next),
                        style = ThemeTypography.button,
                        color = ThemeColor.Secondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ResultPage(viewModel: MainViewModel, onPlayAgainClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(id = if (viewModel.findFalconResponse?.status?.toLowerCase() == "success") R.string.congratulations else R.string.failed),
                style = ThemeTypography.h5,
                color = ThemeColor.Primary,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = if (viewModel.showError) stringResource(id = R.string.error)
                else if (viewModel.findFalconResponse?.status?.toLowerCase() == "success") stringResource(id = R.string.congratulations_text)
                else if (!viewModel.findFalconResponse?.error.isNullOrEmpty()) viewModel.findFalconResponse?.error.orEmpty()
                else stringResource(id = R.string.failed_text),
                style = ThemeTypography.subtitle1,
                color = ThemeColor.Primary,
                textAlign = TextAlign.Center
            )

            if (viewModel.findFalconResponse?.status?.toLowerCase() == "success") {
                val image = when (viewModel.findFalconResponse?.planetName?.toLowerCase()) {
                    "donlon" -> R.drawable.donlon
                    "enchai" -> R.drawable.enchai
                    "jebing" -> R.drawable.jebing
                    "lerbin" -> R.drawable.lerbin
                    "pingasor" -> R.drawable.pingasor
                    "sapir" -> R.drawable.sapir
                    else -> R.drawable.planet
                }

                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = viewModel.findFalconResponse?.planetName.orEmpty(),
                    style = ThemeTypography.h6,
                    color = ThemeColor.Primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "Time taken: ${viewModel.timeTaken} hours",
                    style = ThemeTypography.subtitle1,
                    color = ThemeColor.Primary,
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.BottomCenter),
            enabled = viewModel.selectedPlanet.isNotEmpty() && viewModel.selectedVehicle.isNotEmpty(),
            shape = RoundedCornerShape(16.dp),
            onClick = onPlayAgainClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ThemeColor.Primary,
                contentColor = ThemeColor.Grey20
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = stringResource(id = R.string.play_again),
                style = ThemeTypography.button,
                color = ThemeColor.Secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PlanetSection(viewModel: MainViewModel, onClick: (Planet) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = stringResource(id = R.string.select_planet),
            style = ThemeTypography.subtitle2,
            color = ThemeColor.Primary,
            textAlign = TextAlign.Start
        )

        val itemWidth = (getScreenWidth() / 2) - 24.dp
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            viewModel.planetList.forEachIndexed { i, planet ->
                item(key = "planet_$i") {
                    PlanetCard(
                        itemWidth = itemWidth,
                        planet = planet,
                        selected = planet.name == viewModel.selectedPlanet,
                        isPicked = planet.selected,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
fun PlanetCard(
    itemWidth: Dp,
    planet: Planet,
    selected: Boolean = false,
    isPicked: Boolean = false,
    onClick: (Planet) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(itemWidth)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable { if (!isPicked) onClick(planet) },
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        color = if (isPicked) ThemeColor.Grey90 else ThemeColor.Secondary
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (!isPicked) {
                Image(
                    painter = painterResource(id = if (selected) R.drawable.ic_checked else R.drawable.ic_unchecked),
                    contentDescription = "",
                    modifier = Modifier
                        .size(34.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = planet.image ?: R.drawable.planet),
                    contentDescription = "",
                    modifier = Modifier.size(itemWidth / 2)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .widthIn(max = itemWidth)
                        .padding(horizontal = 16.dp),
                    text = planet.name.orEmpty(),
                    style = ThemeTypography.subtitle1,
                    color = ThemeColor.Primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .widthIn(max = itemWidth)
                        .padding(horizontal = 16.dp),
                    text = "Distance: ${planet.distance.toString()}",
                    style = ThemeTypography.caption,
                    color = ThemeColor.Primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun VehicleSection(viewModel: MainViewModel, onClick: (Vehicle) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = stringResource(id = R.string.select_vehicle),
            style = ThemeTypography.subtitle2,
            color = ThemeColor.Primary,
            textAlign = TextAlign.Start
        )

        val itemWidth = (getScreenWidth() / 2) - 24.dp
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            viewModel.vehicleList.forEachIndexed { i, vehicle ->
                item(key = "vehicle_$i") {
                    VehicleCard(
                        itemWidth = itemWidth,
                        vehicle = vehicle,
                        selected = vehicle.name == viewModel.selectedVehicle,
                        isPicked = vehicle.totalNo!! < 1,
                        onClick = onClick
                    )
                }
            }
        }
    }
}


@Composable
fun VehicleCard(
    itemWidth: Dp,
    vehicle: Vehicle,
    selected: Boolean = false,
    isPicked: Boolean = false,
    onClick: (Vehicle) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(itemWidth)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable { if (!isPicked) onClick(vehicle) },
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        color = if (isPicked) ThemeColor.Grey90 else ThemeColor.Secondary
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (!isPicked) {
                Image(
                    painter = painterResource(id = if (selected) R.drawable.ic_checked else R.drawable.ic_unchecked),
                    contentDescription = "",
                    modifier = Modifier
                        .size(34.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = vehicle.image ?: R.drawable.rocket),
                    contentDescription = "",
                    modifier = Modifier.size(itemWidth / 2)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .widthIn(max = itemWidth)
                        .padding(horizontal = 16.dp),
                    text = vehicle.name.orEmpty(),
                    style = ThemeTypography.subtitle1,
                    color = ThemeColor.Primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .widthIn(max = itemWidth)
                        .padding(horizontal = 16.dp),
                    text = "Max Distance: ${vehicle.maxDistance.toString()}",
                    style = ThemeTypography.caption,
                    color = ThemeColor.Primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    modifier = Modifier
                        .widthIn(max = itemWidth)
                        .padding(horizontal = 16.dp),
                    text = "Speed: ${vehicle.speed.toString()}",
                    style = ThemeTypography.caption,
                    color = ThemeColor.Primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    modifier = Modifier
                        .widthIn(max = itemWidth)
                        .padding(horizontal = 16.dp),
                    text = "Total No: ${vehicle.totalNo.toString()}",
                    style = ThemeTypography.caption,
                    color = ThemeColor.Primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun getScreenWidth(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.dp
}
