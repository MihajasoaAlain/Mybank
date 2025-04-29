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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.api.data.dto.StatsResponse
import com.example.mybank.api.repository.RemoteClientRepository

@Composable
fun StatistiquesScreen(repository: RemoteClientRepository) {
    // Couleurs style iOS
    val iosPrimaryColor = Color(0xFF007AFF)
    val iosSurfaceColor = Color(0xFFF2F5F9)
    val iosGreen = Color(0xFF34C759)
    val iosYellow = Color(0xFFFFCC00)
    val iosRed = Color(0xFFFF3B30)
    val headerColor = Color(0xFFF8F8F8)

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

                // Carte du résumé des statistiques
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Résumé des comptes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Divider élégant
                        Divider(
                            color = iosPrimaryColor.copy(alpha = 0.1f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        StatsRowIOS(
                            "Nombre de clients",
                            clients.size.toString(),
                            iosPrimaryColor
                        )

                        stats?.let { statsData ->
                            StatsRowIOS(
                                "Solde total",
                                String.format("%.2f €", statsData.total),
                                iosPrimaryColor
                            )
                            StatsRowIOS(
                                "Solde minimal",
                                String.format("%.2f €", statsData.min),
                                iosPrimaryColor
                            )
                            StatsRowIOS(
                                "Solde maximal",
                                String.format("%.2f €", statsData.max),
                                iosPrimaryColor
                            )

                            if (clients.isNotEmpty()) {
                                StatsRowIOS(
                                    "Solde moyen",
                                    String.format("%.2f €", statsData.total / clients.size),
                                    iosPrimaryColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Graphique à barres stylisé
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Répartition des soldes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Divider élégant
                        Divider(
                            color = iosPrimaryColor.copy(alpha = 0.1f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Graphique à barres amélioré
                        BarChartIOS(
                            clients = clients,
                            iosGreen = iosGreen,
                            iosYellow = iosYellow,
                            iosRed = iosRed
                        )
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
fun BarChartIOS(
    clients: List<ClientBancaire>,
    iosGreen: Color,
    iosYellow: Color,
    iosRed: Color
) {
    if (clients.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aucune donnée disponible",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        return
    }

    val maxSolde = clients.maxOf { it.solde } * 1.1f
    val minSolde = clients.minOf { it.solde }
    val adjustedMax = if (minSolde < 0) maxSolde - minSolde else maxSolde
    val axisColor = Color(0xFFD0D0D0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 50.dp, end = 8.dp, top = 16.dp, bottom = 40.dp)
    ) {
        // Lignes horizontales de référence (grille)
        val steps = 5
        for (i in 0..steps) {
            val yPosition = (1f - i.toFloat() / steps) * 100f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(axisColor.copy(alpha = 0.3f))
                    .align(Alignment.TopStart)
                    .offset(y = yPosition.dp)
            )
        }

        // Axe Y
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(axisColor)
                .align(Alignment.CenterStart)
        )

        // Axe X
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(axisColor)
                .align(Alignment.BottomCenter)
        )

        // Barres du graphique
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            clients.forEach { client ->
                val barHeight = if (minSolde < 0) {
                    ((client.solde - minSolde) / adjustedMax * 100f).toFloat()
                } else {
                    (client.solde / maxSolde * 100f).toFloat()
                }

                val barColor = when (client.getCategorieSolde()) {
                    "insuffisant" -> iosRed
                    "moyen" -> iosYellow
                    else -> iosGreen
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .fillMaxHeight(barHeight / 100f)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        barColor.copy(alpha = 0.9f),
                                        barColor.copy(alpha = 0.5f)
                                    )
                                )
                            )
                    )
                    // Nom du client sous la barre
                    Text(
                        text = client.nom.split(" ")[0],
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(40.dp)
                            .padding(top = 4.dp)
                    )
                    // Valeur de solde sous le nom
                    Text(
                        text = String.format("%.0f €", client.solde),
                        fontSize = 10.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Légende de l'axe Y (valeurs de solde)
        for (i in 0..steps) {
            val value = minSolde + (maxSolde - minSolde) * i / steps
            val yPosition = (1f - i.toFloat() / steps) * 100f

            Box(
                modifier = Modifier
                    .offset(y = yPosition.dp)
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = String.format("%.0f €", value),
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.offset(x = (-40).dp)
                )
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(1.dp)
                        .background(axisColor)
                        .offset(x = (-3).dp)
                )
            }
        }
    }

    // Légende des couleurs
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 8.dp, top = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        LegendItem("Insuffisant", iosRed)
        Spacer(modifier = Modifier.width(16.dp))
        LegendItem("Moyen", iosYellow)
        Spacer(modifier = Modifier.width(16.dp))
        LegendItem("Bon", iosGreen)
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