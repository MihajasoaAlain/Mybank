package com.example.mybank.ui.screens.main

import ClientBancaire
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.api.data.dto.StatsResponse
import com.example.mybank.api.repository.RemoteClientRepository
import com.example.mybank.ui.components.DataVisualizationScreen
import com.example.mybank.ui.components.TotalMaxMinBarChart
import com.example.mybank.utils.MaterialState
import com.patrykandpatrick.vico.core.common.LegendItem

@Composable
fun StatistiquesScreen(repository: RemoteClientRepository) {
    // Couleurs style iOS
    val iosPrimaryColor = Color(0xFF007AFF)
    val iosSurfaceColor = Color(0xFFF2F5F9)
    val iosGreen = Color(0xFF34C759)
    val iosYellow = Color(0xFFFFCC00)
    val iosRed = Color(0xFFFF3B30)
    val headerColor = Color(0xFFF8F8F8)


    val pieColors = mapOf(
        MaterialState.TOTAL to Color(0xFF4CAF50),   // Vert
        MaterialState.MAX to Color(0xFFF44336), // Rouge
        MaterialState.MIN to Color(0xFFFFC107)   // Jaune
    )


    val pieData = listOf(
        MaterialState.TOTAL to 40,
        MaterialState.MAX to 20,
        MaterialState.MIN to 10
    )


    var isLoading by remember { mutableStateOf(true) }
    var clients by remember { mutableStateOf<List<ClientBancaire>>(emptyList()) }
    var stats by remember { mutableStateOf<StatsResponse?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        try {
            clients = repository.getClients()
            stats = repository.getStats()
            isLoading = false
        } catch (e: Exception) {
            error = "Erreur lors du chargement des clients: ${e.message}"
            isLoading = false
        }
    }

    when {
        isLoading -> {
            // Afficher un indicateur de chargement
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            // Afficher le message d'erreur
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = error!!, color = Color.Red)
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(iosSurfaceColor)
                    .padding(16.dp)
            ) {
                // En-tête élégant
                Text(
                    text = "Statistiques",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )


                Spacer(modifier = Modifier.height(16.dp))
                if (stats == null) {
                    // Affichage du loading
                    CircularProgressIndicator()
                } else {
                    Column(
                    ) {

                        val dataList = mutableListOf(
                            stats!!.total.toInt(),
                            stats!!.max.toInt(),
                            stats!!.min.toInt()
                        )
                        val floatValue = mutableListOf<Float>()
                        val datesList = mutableListOf(2, 3, 4)

                        dataList.forEachIndexed { index, value ->

                            floatValue.add(
                                index = index,
                                element = value.toFloat() / dataList.max().toFloat()
                            )

                        }

                        // On utilise une variable locale pour accéder à stats en toute sécurité
                        val currentStats = stats
                        if (currentStats == null) {
                            // Affichage du loading
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {

                            TotalMaxMinBarChart(currentStats.total,currentStats.max,currentStats.min)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun StatsRowIOS(label: String, value: String, iosPrimaryColor: Color) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 15.sp,
                color = Color.DarkGray
            )
            Text(
                text = value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = iosPrimaryColor
            )
        }
    }

    @Composable
    fun LegendItem(label: String, color: Color) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}